import axios, { type AxiosInstance, type AxiosResponse } from 'axios';
import type { IResponseInstallError } from '@/types/api';
import type {
  IControlGitRepository,
  IFilterSearchGitRepository,
  IGetFileFromGitRepository,
  IResponseFileFromGitRepository,
  IResponseGitRepository,
  IResponseInstallingGitRepository,
  IResponseSearchByGrepGitRepository
} from '@/types/gitRepository';
import { type IInstallGitRepository } from '@/types/gitRepository';
import type {
  IDeleteGitProvider,
  IInstallGitProvider,
  ILoadRepoGitProvider,
  IResponseGitProvider,
  IResponseGitRepositoryInProvider,
  IResponseInstallingGitProvider
} from '@/types/gitProvider';
import { GitProviderType } from '@/enums/gitProvider';
import type {
  ICreateMatcherNote,
  IEditMatcherNote,
  IResponseCreateMatcherNote,
  IResponseMatchNotes
} from '@/types/matcherNote';
import type {
  ICreateGitFilterSearch,
  IResponseCreateFilterSearch,
  IResponseGitFilterSearch
} from '@/types/gitFilterSearch';
import type {
  IResponseProjectFileCommit,
  IResponseProjectItemFile,
  IResponseProjectFileAllCommits,
  IResponseProjectFileTodos,
  IResponseProjectFileInfoCountFile,
  IResponseProjectFileComment
} from '@/types/project';
import { TypeFileLineCode } from '@/enums/project';

const apiClient: AxiosInstance = axios.create({
  baseURL: `${import.meta.env.VITE_FD_HOST_API}/api/v1`,
  headers: {
    'Content-type': 'application/json'
  },
  validateStatus: null
});

export default {
  async getAllGitRepositories(): Promise<AxiosResponse<IResponseGitRepository>> {
    return apiClient.get<IResponseGitRepository>('/git/repo');
  },
  async installGitRepository(
    payload: IInstallGitRepository
  ): Promise<AxiosResponse<IResponseInstallingGitRepository | IResponseInstallError>> {
    return apiClient.post('/git/repo/install', {
      ...payload
    });
  },
  async deleteGitRepository(data: IControlGitRepository): Promise<any> {
    return apiClient.delete('/git/repo/delete', {
      data
    });
  },
  async updateGitRepository(data: IControlGitRepository): Promise<any> {
    return apiClient.put('/git/repo/fetch', {
      ...data
    });
  },
  async checkoutGitRepository(data: IControlGitRepository): Promise<any> {
    return apiClient.put('/git/repo/checkout', {
      ...data
    });
  },
  async searchByGrep(
    data: IFilterSearchGitRepository
  ): Promise<AxiosResponse<IResponseSearchByGrepGitRepository>> {
    return apiClient.post('/operation/git-search', {
      filters: data.filters,
      pattern: data.pattern
    });
  },
  async getFileContent(
    data: IGetFileFromGitRepository
  ): Promise<AxiosResponse<IResponseFileFromGitRepository>> {
    return apiClient.post('/operation/git-get-file', {
      ...data
    });
  },

  async getAllGitProviders(): Promise<AxiosResponse<IResponseGitProvider>> {
    return apiClient.get<IResponseGitProvider>('/git/provider');
  },
  async getAllGitRepositoriesInProvider(
    provider: GitProviderType,
    target: string
  ): Promise<AxiosResponse<IResponseGitRepositoryInProvider>> {
    return apiClient.get<IResponseGitRepositoryInProvider>('/operation/git-get-repo-provider', {
      params: {
        provider,
        target
      }
    });
  },
  async installGitProvider(
    data: IInstallGitProvider
  ): Promise<AxiosResponse<IResponseInstallingGitProvider | IResponseInstallError>> {
    return apiClient.post('/git/provider/install', {
      ...data
    });
  },
  async deleteGitProvider(data: IDeleteGitProvider): Promise<any> {
    return apiClient.delete('/git/provider/delete', {
      data
    });
  },
  async loadRepoFromRemoteHost(data: ILoadRepoGitProvider): Promise<void> {
    await apiClient.post('/operation/git-load-repo-remote', {
      ...data
    });
  },

  async getAllMatcherNotes(): Promise<AxiosResponse<IResponseMatchNotes>> {
    return apiClient.get('/git/matcher/note');
  },
  async createMatcherNote(
    data: ICreateMatcherNote
  ): Promise<AxiosResponse<IResponseCreateMatcherNote>> {
    return apiClient.post('/git/matcher/note', {
      ...data
    });
  },
  async editMatcherNote(
    id: number,
    data: IEditMatcherNote
  ): Promise<AxiosResponse<IResponseCreateMatcherNote>> {
    return apiClient.put(`/git/matcher/note/${id}`, {
      ...data
    });
  },
  async deleteMatcherNote(id: number): Promise<void> {
    await apiClient.delete(`/git/matcher/note/${id}`);
  },

  async getAllGitFilterSearch(): Promise<AxiosResponse<IResponseGitFilterSearch>> {
    return apiClient.get('/git/filter/search');
  },
  async deleteGitFilterSearch(id: number): Promise<void> {
    await apiClient.delete(`/git/filter/search/${id}`);
  },
  async createGitFilterSearch(
    data: ICreateGitFilterSearch
  ): Promise<AxiosResponse<IResponseCreateFilterSearch>> {
    return apiClient.post('/git/filter/search', {
      ...data
    });
  },

  async getProjectFiles(id: number): Promise<AxiosResponse<IResponseProjectItemFile>> {
    return await apiClient.post(`/project/files`, {
      gitRepositoryId: id,
      isTreeStructure: true
    });
  },
  async getProjectFileFirstCommit(
    id: number,
    file: string
  ): Promise<AxiosResponse<IResponseProjectFileCommit>> {
    return await apiClient.post(`/project/files/first-commit`, {
      gitRepositoryId: id,
      file: file
    });
  },
  async getProjectFileLastCommit(
    id: number,
    file: string
  ): Promise<AxiosResponse<IResponseProjectFileCommit>> {
    return await apiClient.post(`/project/files/last-commit`, {
      gitRepositoryId: id,
      file: file
    });
  },
  async getProjectFileAllCommit(
    id: number,
    file: string
  ): Promise<AxiosResponse<IResponseProjectFileAllCommits>> {
    return await apiClient.post(`/project/files/all-commit`, {
      gitRepositoryId: id,
      file: file
    });
  },
  async getProjectFileTodos(
    id: number,
    filters: {
      file: string;
      isFile: boolean;
      isDirectory: boolean;
    }
  ): Promise<AxiosResponse<IResponseProjectFileTodos>> {
    return await apiClient.post(`/project/files/todo`, {
      gitRepositoryId: id,
      filters
    });
  },
  async getProjectFileInfoCountLine(
    id: number,
    file: string,
    type: TypeFileLineCode
  ): Promise<AxiosResponse<IResponseProjectFileInfoCountFile>> {
    return await apiClient.post(`/project/files/lines-code`, {
      gitRepositoryId: id,
      file: file,
      type
    });
  },
  async getProjectFileComments(): Promise<AxiosResponse<IResponseProjectFileComment>> {
    return await apiClient.get('/project/files/comment');
  }
};
