import type { IGitRepository } from '@/types/gitRepository';
import type { IProject } from '@/types/project';

export const conversionGitRepositoryToProject = (gitRepositories: IGitRepository[]): IProject[] => {
  const projects: IProject[] = [];

  gitRepositories.forEach((gitRepository: IGitRepository): void => {
    projects.push({
      id: gitRepository.id,
      group: gitRepository.group_,
      project: gitRepository.project
    });
  });

  return projects;
};
