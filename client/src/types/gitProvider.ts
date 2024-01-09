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
