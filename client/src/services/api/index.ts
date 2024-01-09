import axios, { type AxiosInstance, type AxiosResponse } from 'axios';
import type {
  IControlGitRepository,
  IFilterSearchGitRepository,
  IResponseGitRepository,
  IResponseInstallingGitRepository,
  IResponseInstallingGitRepositoryError,
  IResponseSearchByGrepGitRepository
} from '@/types/gitRepository';
import { type IInstallGitRepository } from '@/types/gitRepository';
import type { IResponseGitProvider } from '@/types/gitProvider';

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
  ): Promise<
    AxiosResponse<IResponseInstallingGitRepository | IResponseInstallingGitRepositoryError>
  > {
    return apiClient.post('/git/repo/install', {
      ...payload
    });
  },
  async deleteGitRepository(data: IControlGitRepository): Promise<any> {
    return apiClient.delete('/git/repo/delete', {
      data
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

  async getAllGitProviders(): Promise<AxiosResponse<IResponseGitProvider>> {
    return apiClient.get<IResponseGitProvider>('/git/provider');
  }
};
