import { defineStore } from 'pinia';
import type { Ref } from 'vue';
import { ref } from 'vue';
import type { IGitProvider } from '@/types/gitProvider';
import api from '@/services/api';

export const useGitProviderState = defineStore('gitProvider', () => {
  const gitProviders: Ref<IGitProvider[]> = ref([]);
  const isLoading: Ref<boolean> = ref(true);
  const isLoadData: Ref<boolean> = ref(false);

  const loadGitProviders = async (): Promise<void> => {
    if (isLoadData.value) return;

    isLoading.value = true;
    gitProviders.value = (await api.getAllGitProviders()).data.gitProviders;
    isLoadData.value = true;
    isLoading.value = false;
  };

  return {
    loadGitProviders,

    gitProviders,
    isLoading,
    isLoadData
  };
});
