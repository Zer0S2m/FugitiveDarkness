export interface IGitRepository {
  id: number;
  group_: string;
  project: string;
  host: string;
  created_at: string;
  is_load: boolean;
  is_unpacking: boolean;
  is_local: boolean;
}

export interface IInstallGitRepository {
  remote: string;
  isLocal: boolean;
}

export interface IResponseGitRepository {
  success: boolean;
  gitRepositories: IGitRepository[];
}

export interface IResponseInstallingGitRepository {
  success: boolean;
  isLoadGitRepository: boolean;
  isLocalGitRepository: boolean;
  gitRepository: {
    host: string;
    group: string;
    project: string;
  };
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
  includeExtensionFiles: string[];
  excludeExtensionFiles: string[];
  patternForIncludeFile: string;
  patternForExcludeFile: string;
  maxCount: number;
  maxDepth: number;
  context: number;
  contextBefore: number;
  contextAfter: number;
}

export interface IMatcherFoundGroupByGrepGitRepository {
  group: string;
  start: number;
  end: number;
}

export interface IMatcherFoundByGrepGitRepository {
  matcher: string;
  link: string;
  lineNumber: number;
  groups: IMatcherFoundGroupByGrepGitRepository[];
  previewLast: IMatcherFoundByGrepGitRepository[] | null;
  previewNext: IMatcherFoundByGrepGitRepository[] | null;
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

export interface IGetFileFromGitRepository {
  group: string;
  project: string;
  file: string;
}

export interface IResponseFileFromGitRepositoryContent {
  lineNumber: number;
  line: string;
}

export interface IResponseFileFromGitRepository {
  success: boolean;
  content: IResponseFileFromGitRepositoryContent[];
}
