import { defineStore } from 'pinia';
import { type Ref, ref } from 'vue';
import type {
  ICreateGitFilterSearch,
  IGitFilterSearch,
  IResponseCreateFilterSearch,
  IResponseGitFilterSearch
} from '@/types/gitFilterSearch';
import api from '@/services/api';
import { type IFilterSearchGitRepository } from '@/types/gitRepository';

export const useGitFilterSearchState = defineStore('gitFilterSearch', () => {
  const isLoad: Ref<boolean> = ref(false);
  const isLoading: Ref<boolean> = ref(true);
  const gitGitFiltersSearch: Ref<IGitFilterSearch[]> = ref([]);
  const activeFilterForCreate: Ref<IFilterSearchGitRepository | null> = ref(null);

  const loadGitFilterSearch = async (): Promise<void> => {
    if (isLoad.value) return;

    isLoading.value = true;

    const response: IResponseGitFilterSearch = (await api.getAllGitFilterSearch()).data;
    gitGitFiltersSearch.value = response.filtersSearch;

    isLoad.value = true;
    isLoading.value = false;
  };

  const deleteGitFilterSearch = async (id: number): Promise<void> => {
    await api.deleteGitFilterSearch(id);

    gitGitFiltersSearch.value = gitGitFiltersSearch.value.filter(
      (gtiFilterSearch: IGitFilterSearch): boolean => {
        return gtiFilterSearch.id !== id;
      }
    );
  };

  const createGitFilterSearch = async (payload: ICreateGitFilterSearch): Promise<void> => {
    const newFilter: IResponseCreateFilterSearch = (await api.createGitFilterSearch(payload)).data;

    gitGitFiltersSearch.value.push(newFilter.filter);
  };

  return {
    isLoad,
    isLoading,
    gitGitFiltersSearch,
    activeFilterForCreate,

    loadGitFilterSearch,
    deleteGitFilterSearch,
    createGitFilterSearch
  };
});
