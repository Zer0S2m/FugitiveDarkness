import type { GitJobType } from '@/enums/gitJob';

export interface IGitJob {
  id: number;
  created_at: string;
  next_run_at: string;
  cron: string;
  git_repository_id: number;
  type: GitJobType;
}

export interface IResponseGitJob {
  success: boolean;
  gitJobs: IGitJob[];
}

export interface IGitJobCreate {
  cron: string;
  gitRepositoryId: number;
  type: GitJobType;
}

export interface IResponseGitJobCreate {
  success: boolean;
  gitJob: IGitJob;
}

export interface IGitJobEdit {
  cron: string;
}
