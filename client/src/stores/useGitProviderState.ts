import { defineStore } from 'pinia';
import type { Ref } from 'vue';
import { ref } from 'vue';
import type { IGitProvider } from '@/types/gitProvider';
import api from '@/services/api';
import type { IGitRepositoryInProvider } from '@/types/gitProvider';
import { GitProviderType } from '@/enums/gitProvider';

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

  return {
    loadGitProviders,
    loadGitRepositoryInProvider,
    getRepositoryInProviderByTypeAndTarget,
    initGitRepositoryInProvider,
    getIsLoadingRepositoryInProviderByTypeAndTarget,

    gitProviders,
    isLoading,
    isLoadData,
    gitRepositoriesInProvider
  };
});
