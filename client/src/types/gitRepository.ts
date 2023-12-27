export interface IGitRepository {
  id: number;
  group_: string;
  project: string;
  host: string;
  create_at: string;
  is_load: boolean;
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
  lineNumber: number;
}

export interface ISearchFoundByGrepGitRepository {
  filename: string;
  extension: string;
  matchers: IMatcherFoundByGrepGitRepository[];
}

export interface ISearchByGrepGitRepository {
  group: string;
  project: string;
  pattern: string;
  extensionFiles: string[];
  found: ISearchFoundByGrepGitRepository[];
}

export interface IResponseSearchByGrepGitRepository {
  success: boolean;
  searchResult: ISearchByGrepGitRepository[];
}
