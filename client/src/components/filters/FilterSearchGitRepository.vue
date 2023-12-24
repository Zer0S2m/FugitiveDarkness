<template>
  <div class="filter-search">
    <div class="filter-search__wrapper">
      <h5 class="filter-search__title">Filter</h5>
      <div class="filter-search__tool">
        <ul>
          <li>
            <h6>Repositories</h6>
            <ul class="filter-search__tool-repositories" v-if="!useGitRepositoryStore.isLoading">
              <li class="filter-search__tool-repository" v-for="gitRepository in useGitRepositoryStore.gitRepositories">
                <Checkbox :meta="gitRepository" :title=" gitRepository.project" :click-handler="handlerClickCheckbox"
                          :is-activity="useGitRepositoryStore.getIsActivityGitRepositoryInFilter({
                            group: gitRepository.group_,
                            project: gitRepository.project
                          })"/>
              </li>
            </ul>
            <div class="filter-search__tool-repositories--loader" v-if="useGitRepositoryStore.isLoading">
              <HalfCircleSpinner
                  :animation-duration="1000"
                  :size="24"
                  color="var(--color-secondary)"
              />
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Checkbox from "@/components/common/Checkbox.vue";
import {useGitRepositoryState} from "@/stores/useGitRepositoryState";
import {HalfCircleSpinner} from "epic-spinners";
import type {IGitRepository} from "@/types/gitRepository";

const useGitRepositoryStore = useGitRepositoryState()
useGitRepositoryStore.loadGitRepositories()

const handlerClickCheckbox = (meta: IGitRepository, isActivity: boolean) => {
  if (isActivity) {
    useGitRepositoryStore.setGitRepositoryFilterSearch({
      group: meta.group_,
      project: meta.project,
    })
  } else if (!isActivity) {
    useGitRepositoryStore.removeGitRepositoryFilterSearch({
      group: meta.group_,
      project: meta.project,
    })
  }
}
</script>

<style scoped>
.filter-search {
  width: 100%;
  border: 1px solid var(--color-border);
  border-radius: 4px;
}

.filter-search__title {
  margin-bottom: 20px;
  font-size: 18px;
}

.filter-search__wrapper {
  padding: 12px;
}

.filter-search__tool {
  margin-bottom: 20px;
}

.filter-search__tool-repositories {
  margin-top: 12px;
}

.filter-search__tool-repository {
  margin-bottom: 8px;
}

.filter-search__tool-repositories--loader {
  margin-top: 12px;
  display: flex;
  justify-content: center;
}
</style>