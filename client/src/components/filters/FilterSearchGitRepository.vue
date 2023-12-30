<template>
  <div class="filter-search">
    <div class="filter-search__wrapper">
      <h5 class="filter-search__title">Filter</h5>
      <div class="filter-search__tool">
        <ul>
          <li>
            <h6>Repositories</h6>
            <ul
              class="filter-search__tool-repositories"
              v-if="!useGitRepositoryStore.isLoading"
            >
              <li
                class="filter-search__tool-repository"
                v-for="gitRepository in useGitRepositoryStore.gitRepositories"
              >
                <Checkbox
                  :meta="gitRepository"
                  :title="gitRepository.project"
                  :click-handler="handlerClickCheckboxGitRepository"
                  :is-activity="
                    useGitRepositoryStore.getIsActivityGitRepositoryInFilter({
                      group: gitRepository.group_,
                      project: gitRepository.project
                    })
                  "
                />
                <div
                  class="filter-search__tool-repository--extension_files"
                  v-if="
                    useGitRepositoryStore.hasExtensionFilesForFilterByRepository(
                      `${gitRepository.group_}/${gitRepository.project}`
                    )
                  "
                >
                  <h6>File extension</h6>
                  <ul>
                    <li
                      class="filter-search__tool-repository--extension_file"
                      v-for="filtersByExtensionFile in useGitRepositoryStore.filtersByExtensionFiles.get(
                        `${gitRepository.group_}/${gitRepository.project}`
                      )"
                    >
                      <Checkbox
                        :meta="{
                          extensionFile: filtersByExtensionFile.extension,
                          ...gitRepository
                        }"
                        :title="filtersByExtensionFile.extension"
                        :click-handler="handlerClickCheckboxExtensionFile"
                        :is-activity="filtersByExtensionFile.isActive"
                      />
                    </li>
                  </ul>
                </div>
              </li>
            </ul>
            <div
              class="filter-search__tool-repositories--loader"
              v-if="useGitRepositoryStore.isLoading"
            >
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
import Checkbox from '@/components/common/Checkbox.vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { HalfCircleSpinner } from 'epic-spinners';
import type { IGitRepository } from '@/types/gitRepository';

const useGitRepositoryStore = useGitRepositoryState();
useGitRepositoryStore.loadGitRepositories();

interface IGitRepositoryFileExtension extends IGitRepository {
  extensionFile: string;
}

const handlerClickCheckboxGitRepository = (meta: IGitRepository, isActivity: boolean) => {
  if (isActivity) {
    useGitRepositoryStore.setGitRepositoryFilterSearch({
      group: meta.group_,
      project: meta.project
    });
  } else if (!isActivity) {
    useGitRepositoryStore.removeGitRepositoryFilterSearch({
      group: meta.group_,
      project: meta.project
    });
  }

  useGitRepositoryStore.setFilterByRepository(`${meta.group_}/${meta.project}`, isActivity);
};

const handlerClickCheckboxExtensionFile = (
  meta: IGitRepositoryFileExtension,
  isActivity: boolean
) => {
  useGitRepositoryStore.setExtensionFileForFilter(
    `${meta.group_}/${meta.project}`,
    meta.extensionFile,
    isActivity,
    true
  );
};
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

.filter-search__tool-repository--extension_files {
  margin: 8px 0 0 12px;
}

.filter-search__tool-repository--extension_files > h6 {
  margin-bottom: 4px;
  font-size: 14px;
}

.filter-search__tool-repository--extension_file {
  margin-bottom: 4px;
}

.filter-search__tool-repositories--loader {
  margin-top: 12px;
  display: flex;
  justify-content: center;
}
</style>
