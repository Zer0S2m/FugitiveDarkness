import {defineStore} from "pinia";
import {
  type IControlGitRepository,
  type IFilterSearchGitRepository,
  type IGitRepository
} from "@/types/gitRepository";
import api from "@/services/api"
import type {Ref} from "vue";
import {ref} from "vue";

export const useGitRepositoryState = defineStore('gitRepository', () => {
  const gitRepositories: Ref<IGitRepository[]> = ref([])
  const isLoading: Ref<boolean> = ref(true)
  const isLoadData: Ref<boolean> = ref(false)
  const filtersForSearch: Ref<IFilterSearchGitRepository> = ref({
    filters: {
      git: [] as IControlGitRepository[]
    },
    pattern: '' as string
  })

  const loadGitRepositories = async () => {
    if (isLoadData.value) return

    isLoading.value = true
    gitRepositories.value = (await api.getAllGitRepositories()).data.gitRepositories
    isLoadData.value = true
    isLoading.value = false
  }

  const deleteGitRepository = async (gitRepo: IControlGitRepository) => {
    await api.deleteGitRepository(gitRepo)

    gitRepositories.value = gitRepositories.value.filter((gitRepository) => {
      return !(gitRepository.group_ === gitRepo.group && gitRepository.project === gitRepo.project)
    })
  }

  const setPatternFilterSearch = (pattern: string) => {
    filtersForSearch.value.pattern = pattern
  }

  const setGitRepositoryFilterSearch = (gitRepository: IControlGitRepository) => {
    filtersForSearch.value.filters.git.push(gitRepository)
  }

  const removeGitRepositoryFilterSearch = (gitRepository: IControlGitRepository) => {
    filtersForSearch.value.filters.git = filtersForSearch.value.filters.git.filter(gitRepository_ => {
      return !(gitRepository_.group === gitRepository.group && gitRepository_.project === gitRepository.project)
    })
  }

  const getIsActivityGitRepositoryInFilter = (gitRepository: IControlGitRepository): boolean => {
    return filtersForSearch.value.filters.git.find(gitRepository_ => {
      return (gitRepository_.group === gitRepository.group && gitRepository_.project === gitRepository.project)
    }) != null
  }

  return {
    loadGitRepositories,
    deleteGitRepository,
    setPatternFilterSearch,
    setGitRepositoryFilterSearch,
    removeGitRepositoryFilterSearch,
    getIsActivityGitRepositoryInFilter,

    gitRepositories,
    isLoading,
    isLoadData,
    filtersForSearch
  }
})
