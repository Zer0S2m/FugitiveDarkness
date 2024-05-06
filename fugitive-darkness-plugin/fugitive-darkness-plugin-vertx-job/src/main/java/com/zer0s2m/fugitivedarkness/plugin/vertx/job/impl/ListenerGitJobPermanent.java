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
 * Listener for scheduled tasks of the type {@link GitTypeJob#PERMANENT}.
 */
public class ListenerGitJobPermanent implements Listener {

    Logger logger = LoggerFactory.getLogger(ListenerGitJobPermanent.class);

    final private FieldBuilder<Row> fieldBuilder = new FieldBuilderRow();

    final private JobManager jobManager = new JobManagerImpl();

    final private Rule ruleIsLocalGitRepo = new RuleIsLocalGitRepo();

    final private Rule ruleIsRepoExists = new RuleISRepoExists();

    {
        ruleIsLocalGitRepo.setTypeTask(GitTypeJob.PERMANENT);
        ruleIsRepoExists.setTypeTask(GitTypeJob.PERMANENT);
    }

    /**
     * Run a listening scan from different sources.
     */
    @Override
    public void listen(final Vertx vertx) {
        final GitJobRepository gitJobRepository = new GitJobRepositoryImpl(vertx);

        logger.info("Listening to tasks with the type [" + GitTypeJob.PERMANENT + "]");

        gitJobRepository
                .findAllByType(GitTypeJob.PERMANENT.name())
                .onSuccess(ar -> {
                    final List<GitJobModel> gitJobs = ListenerGitJobUtils
                            .getGitJobsFromRowSet(ar, fieldBuilder);

                    gitJobs.forEach(gitJob -> {
                        LocalDateTime currentDate = LocalDateTime.now();
                        CronExpression cronExpression = CronExpression.parse(gitJob.getCron());
                        LocalDateTime nextRunAt = cronExpression.next(currentDate);
                        boolean isUpdateRow = false;

                        if (gitJob.getNextRunAt() == null) {
                            isUpdateRow = true;
                        } else {
                            if (currentDate.isAfter(gitJob.getNextRunAt())) {
                                isUpdateRow = true;

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
                            }
                        }

                        if (isUpdateRow) {
                            gitJobRepository
                                    .updateNextRunAtById(nextRunAt, gitJob.getId())
                                    .onFailure(error -> logger.error("Failure (DB) [update next run at]: "
                                            + error.getMessage()));
                        }
                    });
                })
                .onFailure(error -> {
                    gitJobRepository.closeClient();

                    logger.error("Failure (DB) [get data]: " + error.getMessage());
                });
    }

}
