package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerInfoGitProviderControl;
import com.zer0s2m.fugitivedarkness.models.GitProviderModel;
import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.GitRepoProviderConverter;
import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.GitRepoProviderWebClient;
import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.RepositoryInstallerRemoteVertx;
import com.zer0s2m.fugitivedarkness.provider.git.*;
import com.zer0s2m.fugitivedarkness.repository.GitProviderRepository;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Request handler for cloning git repositories from a remote host.
 */
public class ControllerApiGitRepoInstallRemote implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoInstallRemote.class);

    /**
     * Install the repository on the system.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
        final GitRepoRepository gitRepoRepository = new GitRepoRepositoryImpl(event.vertx());
        final GitRepoProviderWebClient gitRepoProviderWebClient = GitRepoProviderWebClient.create(event.vertx());
        final ContainerInfoGitProviderControl containerInfoGitProviderDelete = event
                .body()
                .asJsonObject()
                .mapTo(ContainerInfoGitProviderControl.class);
        final GitRepoProviderType typeProvider = GitRepoProviderType.valueOf(containerInfoGitProviderDelete.type());

        logger.info("Start of cloning [{}:{}]",
                containerInfoGitProviderDelete.type(),
                containerInfoGitProviderDelete.target());

        gitProviderRepository
                .findByTypeAndTarget(typeProvider.toString(), containerInfoGitProviderDelete.target())
                .onComplete(ar -> {
                    if (!ar.succeeded()) {
                        logger.error("Failure (DB): " + ar.cause());
                        return;
                    }

                    final GitProviderModel gitProvider = gitProviderRepository.mapTo(ar.result()).get(0);
                    final GitRepoProvider gitRepoProvider = GitRepoProvider.create(typeProvider);
                    final GitRepoProviderConverter gitRepoProviderConverter = GitRepoProviderConverter.create();

                    GitRepoProviderInfo gitRepoProviderInfo;

                    if (gitProvider.getIsOrg()) {
                        gitRepoProviderInfo = gitRepoProvider
                                .getInfoGitRepositoriesForOrg(gitProvider.getTarget());
                    } else {
                        gitRepoProviderInfo = gitRepoProvider
                                .getInfoGitRepositoriesForUser(gitProvider.getTarget());
                    }

                    gitRepoProviderWebClient
                            .getGitRepositories(gitRepoProviderInfo)
                            .onSuccess(result -> {
                                final AtomicReference<List<ContainerInfoRepo>> readyData = new AtomicReference<>(
                                        gitRepoProviderConverter.convert(typeProvider, result.body()));
                                if (readyData.get().isEmpty()) {
                                    logger.info("There is nothing to clone [{}:{}]",
                                            containerInfoGitProviderDelete.type(),
                                            containerInfoGitProviderDelete.target());
                                    return;
                                }

                                final AtomicReference<List<GitRepoModel>> gitRepoModels =
                                        new AtomicReference<>(new ArrayList<>());
                                readyData.get().forEach((obj) -> gitRepoModels.get().add(new GitRepoModel(
                                        obj.group(),
                                        obj.project(),
                                        obj.host(),
                                        HelperGitRepo.getSourceGitRepository(
                                                obj.group(), obj.project()),
                                        false)));

                                gitRepoRepository
                                        .findAllByGroupAndHost(
                                                containerInfoGitProviderDelete.target(),
                                                gitRepoModels.get().get(0).getHost())
                                        .onSuccess(ar2 -> {
                                            final List<GitRepoModel> gitRepoModelsFromDB = gitRepoRepository.mapTo(ar2);
                                            final List<String> gitReposStr = new ArrayList<>(); // format <host>:<group>:<project>
                                            gitRepoModelsFromDB.forEach(gitRepo -> gitReposStr.add(
                                                    String.format("%s:%s:%s",
                                                            gitRepo.getHost(),
                                                            gitRepo.getGroup(),
                                                            gitRepo.getProject())
                                            ));

                                            // We receive only those repositories that are not in the system
                                            gitRepoModels.set(gitRepoModels
                                                    .get()
                                                    .stream()
                                                    .filter(gitRepoModelFromDB -> !gitReposStr.contains(String.format(
                                                            "%s:%s:%s",
                                                            gitRepoModelFromDB.getHost(),
                                                            gitRepoModelFromDB.getGroup(),
                                                            gitRepoModelFromDB.getProject())))
                                                    .toList()
                                            );
                                            readyData.set(readyData
                                                    .get()
                                                    .stream()
                                                    .filter(gitRepoInfo ->!gitReposStr.contains(String.format(
                                                            "%s:%s:%s",
                                                            gitRepoInfo.host(),
                                                            gitRepoInfo.group(),
                                                            gitRepoInfo.project())))
                                                    .toList());

                                            if (readyData.get().isEmpty()) {
                                                logger.info("There is nothing to clone [{}:{}]",
                                                        containerInfoGitProviderDelete.type(),
                                                        containerInfoGitProviderDelete.target());
                                                return;
                                            }

                                            gitRepoRepository
                                                    .saveAll(gitRepoModels.get())
                                                    .onSuccess(ignored -> {
                                                        gitRepoRepository.closeClient();

                                                        readyData.get().forEach((obj) -> {
                                                            final RepositoryInstallerRemoteVertx installerRemoteVertx =
                                                                    new RepositoryInstallerRemoteVertx(event.vertx());

                                                            Thread.ofVirtual()
                                                                    .name("virtual.git.operation-installing")
                                                                    .start(installerRemoteVertx.runCloneAndSaveDB(
                                                                            RepositoryInstallerRemote
                                                                                    .taskInstallRepoRemote(obj.link()),
                                                                            obj));
                                                        });
                                                    })
                                                    .onFailure(error -> {
                                                        gitRepoRepository.closeClient();
                                                        logger.error("Failure (DB 2): " + error.fillInStackTrace());
                                                    });
                                        })
                                        .onFailure(error -> {
                                            gitRepoRepository.closeClient();
                                            logger.error("Failure (DB 1): " + error.fillInStackTrace());
                                        });
                            })
                            .onFailure(error -> {
                                gitProviderRepository.closeClient();
                                gitRepoRepository.closeClient();
                                logger.error("Failure (HTTP CLIENT): " + error.fillInStackTrace());
                            });
                });

        event
                .response()
                .setStatusCode(HttpResponseStatus.ACCEPTED.code());
        event.next();
    }

}
