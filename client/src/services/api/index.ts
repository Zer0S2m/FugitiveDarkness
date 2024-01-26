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
  async searchByGrep(
    data: IFilterSearchGitRepository
  ): Promise<AxiosResponse<IResponseSearchByGrepGitRepository>> {
    return apiClient.post('/operation/search', {
      filters: data.filters,
      pattern: data.pattern
    });
  },
  async getFileContent(
    data: IGetFileFromGitRepository
  ): Promise<AxiosResponse<IResponseFileFromGitRepository>> {
    return apiClient.post('/operation/get-file-from-git', {
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
    return apiClient.get<IResponseGitRepositoryInProvider>('/operation/get-git-repo-provider', {
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
  }
};
