import { defineStore } from 'pinia';
import type { Ref } from 'vue';
import { ref } from 'vue';
import type {
  IDeleteGitProvider,
  IGitProvider,
  IInstallGitProvider,
  IResponseInstallingGitProvider
} from '@/types/gitProvider';
import api from '@/services/api';
import type { IGitRepositoryInProvider } from '@/types/gitProvider';
import { GitProviderType } from '@/enums/gitProvider';
import type { AxiosResponse } from 'axios';
import type { IResponseInstallError } from '@/types/api';
import { type ILoadRepoGitProvider } from '@/types/gitProvider';

export const useGitProviderState = defineStore('gitProvider', () => {
  const gitProviders: Ref<IGitProvider[]> = ref([]);
  const gitRepositoriesInProvider: Ref<
    Map<
      String,
      {
        isLoading: boolean;
        items: IGitRepositoryInProvider[];
      }
    >
  > = ref(new Map());
  const isLoading: Ref<boolean> = ref(true);
  const isLoadData: Ref<boolean> = ref(false);
  const stateFormAddGitProviderErrors: Ref<{
    success: boolean;
    msg: string | null;
    type: string | null;
  }> = ref({
    success: true,
    msg: null,
    type: null
  });

  const loadGitProviders = async (): Promise<void> => {
    if (isLoadData.value) return;

    isLoading.value = true;
    gitProviders.value = (await api.getAllGitProviders()).data.gitProviders;
    isLoadData.value = true;
    isLoading.value = false;

    initGitRepositoryInProvider();
    await Promise.all(
      gitProviders.value.map(async (gitProvider) => {
        await loadGitRepositoryInProvider(gitProvider.type, gitProvider.target);
      })
    );
  };

  const initGitRepositoryInProvider = () => {
    gitProviders.value.forEach((gitProvider) => {
      gitRepositoriesInProvider.value.set(`${gitProvider.type}/${gitProvider.target}`, {
        isLoading: true,
        items: []
      });
    });
  };

  const loadGitRepositoryInProvider = async (
    type: GitProviderType,
    target: string
  ): Promise<void> => {
    const gitRepositories = (await api.getAllGitRepositoriesInProvider(type, target)).data
      .gitRepositories;

    gitRepositoriesInProvider.value.set(`${type}/${target}`, {
      isLoading: false,
      items: gitRepositories
    });
  };

  const getIsLoadingRepositoryInProviderByTypeAndTarget = (
    type: GitProviderType,
    target: string
  ): boolean => {
    return gitRepositoriesInProvider.value.get(`${type}/${target}`)?.isLoading ?? false;
  };

  const getRepositoryInProviderByTypeAndTarget = (
    type: GitProviderType,
    target: string
  ): IGitRepositoryInProvider[] => {
    if (gitRepositoriesInProvider.value.has(`${type}/${target}`)) {
      return gitRepositoriesInProvider.value.get(`${type}/${target}`)?.items ?? [];
    }

    return [];
  };

  const installingGitProvider = async (payload: IInstallGitProvider): Promise<void> => {
    const resultInstalled: AxiosResponse<IResponseInstallError | IResponseInstallingGitProvider> =
      await api.installGitProvider(payload);

    if ([400, 404].includes(resultInstalled.status)) {
      const resultInstalledData: IResponseInstallError =
        resultInstalled.data as IResponseInstallError;
      stateFormAddGitProviderErrors.value = {
        success: false,
        msg: resultInstalledData.message,
        type: resultInstalledData.type
      };
      return;
    }
  };

  const clearStateFormAddGitProviderErrors = (): void => {
    stateFormAddGitProviderErrors.value = {
      success: true,
      msg: null,
      type: null
    };
  };

  const deleteGitProvider = async (payload: IDeleteGitProvider): Promise<void> => {
    await api.deleteGitProvider(payload);

    gitRepositoriesInProvider.value.delete(`${payload.type}/${payload.target}`);
    gitProviders.value = gitProviders.value.filter((gitProvider) => {
      return !(gitProvider.type === payload.type && gitProvider.target === payload.target);
    });
  };

  const loadRepoFromRemoteHost = async (payload: ILoadRepoGitProvider): Promise<void> => {
    await api.loadRepoFromRemoteHost(payload);
  };

  return {
    loadGitProviders,
    loadGitRepositoryInProvider,
    getRepositoryInProviderByTypeAndTarget,
    initGitRepositoryInProvider,
    getIsLoadingRepositoryInProviderByTypeAndTarget,
    installingGitProvider,
    clearStateFormAddGitProviderErrors,
    deleteGitProvider,
    loadRepoFromRemoteHost,

    gitProviders,
    isLoading,
    isLoadData,
    gitRepositoriesInProvider,
    stateFormAddGitProviderErrors
  };
});
