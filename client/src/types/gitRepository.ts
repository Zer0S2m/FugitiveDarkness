export interface IGitRepository {
  id: number,
  group_: string,
  project: string,
  host: string,
  create_at: string,
  is_load: boolean
}

export interface IResponseGitRepository {
  success: boolean,
  gitRepositories: IGitRepository[]
}

export interface IControlGitRepository {
  group: string,
  project: string
}

export interface IFilterSearchGitRepository {
  filters: IFilterSearchGitRepositories,
  pattern: string
}

export interface IFilterSearchGitRepositories {
  git: IControlGitRepository[]
}
