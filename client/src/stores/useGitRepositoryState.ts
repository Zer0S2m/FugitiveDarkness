import {defineStore} from "pinia";
import {type IDeleteGitRepository, type IGitRepository} from "@/types/gitRepository";
import api from "@/services/api"
import type {Ref} from "vue";
import {ref} from "vue";

export const useGitRepositoryState = defineStore('gitRepository', () => {
  const gitRepositories: Ref<IGitRepository[]> = ref([])
  const isLoading: Ref<boolean> = ref(true)
  const isLoadData: Ref<boolean> = ref(false)
  const searchText: Ref<string> = ref("")

  const loadGitRepositories = async () => {
    if (isLoadData.value) return

    isLoading.value = true
    gitRepositories.value = (await api.getAllGitRepositories()).data.gitRepositories
    isLoadData.value = true
    isLoading.value = false
  }

  const deleteGitRepository = async (gitRepo: IDeleteGitRepository) => {
    await api.deleteGitRepository(gitRepo)

    gitRepositories.value = gitRepositories.value.filter((gitRepository) => {
      return !(gitRepository.group_ === gitRepo.group && gitRepository.project === gitRepo.project)
    })
  }

  return {
    loadGitRepositories,
    deleteGitRepository,

    gitRepositories,
    isLoading,
    isLoadData,
    searchText
  }
})
