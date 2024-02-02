import { GitProviderType } from '@/enums/gitProvider';

export interface IBaseGitProvider {
  type: GitProviderType;
  target: string;
}

export interface IGitProvider extends IBaseGitProvider {
  id: number;
  is_org: boolean;
  is_user: boolean;
  created_at: string;
}

export interface IResponseGitProvider {
  success: boolean;
  gitProviders: IGitProvider[];
}

export interface IGitRepositoryInProvider {
  host: string;
  group: string;
  project: string;
  link: string;
}

export interface IResponseGitRepositoryInProvider {
  success: boolean;
  gitRepositories: IGitRepositoryInProvider[];
}

export interface IInstallGitProvider extends IBaseGitProvider {
  isOrg: boolean;
  isUser: boolean;
}

export interface IResponseInstallingGitProvider {
  success: true;
  gitProvider: IInstallGitProvider;
}

export interface IDeleteGitProvider extends IBaseGitProvider {}

export interface ILoadRepoGitProvider extends IBaseGitProvider {}
