export interface IGitRepository {
  id: number;
  group_: string;
  project: string;
  host: string;
  create_at: string;
  is_load: boolean;
}

export interface IInstallGitRepository {
  remote: string;
  group: string;
}

export interface IResponseGitRepository {
  success: boolean;
  gitRepositories: IGitRepository[];
}

export interface IControlGitRepository {
  group: string;
  project: string;
}

export interface IFilterSearchGitRepository {
  filters: IFilterSearchGitRepositories;
  pattern: string;
}

export interface IFilterSearchGitRepositories {
  git: IControlGitRepository[];
}

export interface IMatcherFoundByGrepGitRepository {
  matcher: string;
  link: string;
  lineNumber: number;
}

export interface ISearchFoundByGrepGitRepository {
  filename: string;
  extension: string;
  link: string;
  matchers: IMatcherFoundByGrepGitRepository[];
}

export interface ISearchByGrepGitRepository {
  group: string;
  project: string;
  pattern: string;
  link: string;
  extensionFiles: string[];
  found: ISearchFoundByGrepGitRepository[];
}

export interface IResponseSearchByGrepGitRepository {
  success: boolean;
  searchResult: ISearchByGrepGitRepository[];
}
