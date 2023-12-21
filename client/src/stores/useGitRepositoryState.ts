import {defineStore} from "pinia";
import {type DeleteGitRepository, type GitRepository} from "@/types/gitRepository";
import type {Ref} from "vue";
import {ref} from "vue";

export const useGitRepositoryState = defineStore('gitRepository', () => {
  const gitRepositories: Ref<GitRepository[]> = ref([])

  const loadGitRepositories = async () => {
    gitRepositories.value = [
      {
        id: 1,
        group_: 'test',
        project: 'test',
        host: 'github.com',
        create_at: '2023-12-16T11:23:53.576425',
        is_load: true
      },
      {
        id: 2,
        group_: 'test_2',
        project: 'test_2',
        host: 'github.com',
        create_at: '2023-12-16T11:23:53.576425',
        is_load: true
      }
    ]
  }

  const deleteGitRepository = async (gitRepo: DeleteGitRepository) => {
    gitRepositories.value = gitRepositories.value.filter((gitRepository) => {
      return !(gitRepository.group_ === gitRepo.group && gitRepo.project === gitRepo.project)
    })
  }

  return {
    loadGitRepositories,
    deleteGitRepository,

    gitRepositories
  }
})
