import type { ITree } from '@/types/tree';

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
