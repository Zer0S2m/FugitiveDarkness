import { GitProviderType } from '@/enums/gitProvider';

export interface IGitProvider {
  id: number;
  type: GitProviderType;
  is_org: boolean;
  is_user: boolean;
  target: string;
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

export interface IInstallGitProvider {
  type: GitProviderType;
  isOrg: boolean;
  isUser: boolean;
  target: string;
}

export interface IResponseInstallingGitProvider {
  success: true;
  gitProvider: IInstallGitProvider;
}
