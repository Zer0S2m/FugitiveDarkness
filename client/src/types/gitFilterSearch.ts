import type { IFilterSearchGitRepository } from '@/types/gitRepository';

export interface IGitFilterSearch {
  id: number;
  createdAt: string;
  title: string;
  filter: IFilterSearchGitRepository;
}

export interface IResponseGitFilterSearch {
  success: boolean;
  filtersSearch: IGitFilterSearch[];
}

export interface ICreateGitFilterSearch {
  title: string;
  filter: IFilterSearchGitRepository;
}

export interface IResponseCreateFilterSearch {
  success: boolean;
  filter: IGitFilterSearch;
}
