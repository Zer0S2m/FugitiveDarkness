import type { ITree } from '@/types/tree';
import type { ISearchFoundByGrepGitRepository } from '@/types/gitRepository';

export interface IProject {
  id: number;
  group: string;
  project: string;
}

export interface IProjectItemFile extends ITree {}

export interface IResponseProjectItemFile {
  success: boolean;
  files: IProjectItemFile;
}

export interface IProjectFileCommit {
  commit: string;
  authorName: string;
  authorEmail: string;
  date: string;
  bodyCommit: string;
}

export interface IResponseProjectFileCommit {
  success: boolean;
  info: IProjectFileCommit;
}

export interface IResponseProjectFileAllCommits {
  success: boolean;
  info: IProjectFileCommit[];
}

export interface IResponseProjectFileTodos {
  success: boolean;
  todos: ISearchFoundByGrepGitRepository[];
}

export interface IProjectFileInfoCountFile {
  path: string;
  countLines: number;
}

export interface IResponseProjectFileInfoCountFile {
  success: boolean;
  codeLines: IProjectFileInfoCountFile[];
}

export interface IProjectFileComment {
  id: number;
  text: string;
  file: string;
  git_repository_id: number;
  created_at: string;
}

export interface IResponseProjectFileComment {
  success: boolean;
  comments: IProjectFileComment[];
}
