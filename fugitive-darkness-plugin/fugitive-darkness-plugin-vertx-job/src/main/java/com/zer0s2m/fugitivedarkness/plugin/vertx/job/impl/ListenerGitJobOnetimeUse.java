package com.zer0s2m.fugitivedarkness.plugin.vertx.job.impl;

import com.zer0s2m.fugitivedarkness.common.mapper.FieldBuilder;
import com.zer0s2m.fugitivedarkness.models.GitJobModel;
import com.zer0s2m.fugitivedarkness.plugin.job.GitTypeJob;
import com.zer0s2m.fugitivedarkness.plugin.job.JobException;
import com.zer0s2m.fugitivedarkness.plugin.job.JobManager;
import com.zer0s2m.fugitivedarkness.plugin.job.Rule;
import com.zer0s2m.fugitivedarkness.plugin.job.impl.JobManagerImpl;
import com.zer0s2m.fugitivedarkness.plugin.job.rule.RuleISRepoExists;
import com.zer0s2m.fugitivedarkness.plugin.job.rule.RuleIsLocalGitRepo;
import com.zer0s2m.fugitivedarkness.plugin.job.support.CronExpression;
import com.zer0s2m.fugitivedarkness.plugin.vertx.job.Listener;
import com.zer0s2m.fugitivedarkness.repository.GitJobRepository;
import com.zer0s2m.fugitivedarkness.repository.mapper.FieldBuilderRow;
import com.zer0s2m.fugitivedarkness.repository.impl.GitJobRepositoryImpl;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Listener for scheduled tasks of the type {@link GitTypeJob#ONETIME_USE}.
 */
public class ListenerGitJobOnetimeUse implements Listener {

    Logger logger = LoggerFactory.getLogger(ListenerGitJobOnetimeUse.class);

    final private FieldBuilder<Row> fieldBuilder = new FieldBuilderRow();

    final private JobManager jobManager = new JobManagerImpl();

    final private Rule ruleIsLocalGitRepo = new RuleIsLocalGitRepo();

    final private Rule ruleIsRepoExists = new RuleISRepoExists();

    {
        ruleIsLocalGitRepo.setTypeTask(GitTypeJob.ONETIME_USE);
        ruleIsRepoExists.setTypeTask(GitTypeJob.ONETIME_USE);
    }

    /**
     * Run a listening scan from different sources.
     */
    @Override
    public void listen(final Vertx vertx) {
        final GitJobRepository gitJobRepository = new GitJobRepositoryImpl(vertx);

        logger.info("Listening to tasks with the type [" + GitTypeJob.ONETIME_USE + "]");

        gitJobRepository
                .findAllByType(GitTypeJob.ONETIME_USE.name())
                .onSuccess(ar -> {
                    final List<GitJobModel> gitJobs = ListenerGitJobUtils
                            .getGitJobsFromRowSet(ar, fieldBuilder);

                    gitJobs.forEach(gitJob -> {
                        LocalDateTime currentDate = LocalDateTime.now();
                        CronExpression cronExpression = CronExpression.parse(gitJob.getCron());
                        LocalDateTime nextRunAt = cronExpression.next(currentDate);

                        if (gitJob.getNextRunAt() == null) {
                            gitJobRepository
                                    .updateNextRunAtById(nextRunAt, gitJob.getId())
                                    .onFailure(error -> logger.error("Failure (DB) [update next run at]: "
                                            + error.getMessage()));
                        } else {
                            if (currentDate.isAfter(gitJob.getNextRunAt())) {
                                try {
                                    jobManager.clearRule();
                                    jobManager.addRule(ruleIsLocalGitRepo);
                                    jobManager.addRule(ruleIsRepoExists);

                                    jobManager.call(
                                            GitTypeJob.PERMANENT,
                                            ListenerGitJobUtils.collectProperties(gitJob));
                                } catch (JobException e) {
                                    logger.error(e.getMessage());
                                    throw new RuntimeException(e);
                                }

                                gitJobRepository
                                        .deleteById(gitJob.getId())
                                        .onFailure(error -> logger.error("Failure (DB) [delete job]: "
                                                + error.getMessage()));
                            }
                        }
                    });
                })
                .onFailure(error -> logger.error("Failure (DB) [get data]: " + error.getMessage()));
    }

}
