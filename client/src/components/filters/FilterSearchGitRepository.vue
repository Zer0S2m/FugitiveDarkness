<template>
  <div class="filter-search">
    <div class="filter-search__wrapper">
      <h5 class="filter-search__title">Filter</h5>
      <div class="filter-search__tool--grep">
        <h6 class="filter-search__tool-title">Command - grep</h6>
        <div class="filter-search__tool tool--grep scroll">
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
                    v-if="gitRepository.is_load"
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
        <div class="filter-search__tool--files">
          <h6 class="filter-search__tool-title--second">Include extension files</h6>
          <TagInput
            @addTag="addIncludeFileExtension"
            @removeTag="removeIncludeFileExtension"
            :tags="includeFilesExtension"
            class="filter-search__tool--files-input"
          />
        </div>
        <div class="filter-search__tool--files">
          <h6 class="filter-search__tool-title--second">Exclude extension files</h6>
          <TagInput
            @addTag="addExcludeFileExtension"
            @removeTag="removeExcludeFileExtension"
            :tags="excludeFilesExtension"
            class="filter-search__tool--files-input"
          />
        </div>
      </div>
    </div>
    <div class="filter-search__reset">
      <div class="filter-search__reset--wrapper">
        <button
          @click="resetResult"
          class="filter-search__reset-btn"
        >
          Reset everything
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Checkbox from '@/components/common/Checkbox.vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { HalfCircleSpinner } from 'epic-spinners';
import type { IGitRepository } from '@/types/gitRepository';
import TagInput from '@/components/common/TagInput.vue';
import { onMounted, type Ref, ref } from 'vue';
import router from '@/router';

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

const resetResult = (): void => {
  useGitRepositoryStore.resetResult();

  includeFilesExtension.value = [];
  excludeFilesExtension.value = [];

  router.push({
    name: 'search',
    query: {}
  });
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

const includeFilesExtension: Ref<string[]> = ref([]);
const excludeFilesExtension: Ref<string[]> = ref([]);

const addIncludeFileExtension = (tag: string): void => {
  // useGitRepositoryStore.addIncludeExtensionFileForFilter(tag);
};

const removeIncludeFileExtension = (index: number): void => {
  useGitRepositoryStore.removeIncludeExtensionFileForFilter(includeFilesExtension.value[index]);
};

const addExcludeFileExtension = (tag: string): void => {
  // useGitRepositoryStore.addExcludeExtensionFileForFilter(tag);
};

const removeExcludeFileExtension = (index: number): void => {
  useGitRepositoryStore.removeExcludeExtensionFileForFilter(excludeFilesExtension.value[index]);
};

onMounted(() => {
  includeFilesExtension.value =
    useGitRepositoryStore.filtersForSearch.filters.includeExtensionFiles;
  excludeFilesExtension.value =
    useGitRepositoryStore.filtersForSearch.filters.excludeExtensionFiles;
});
</script>

<style scoped>
.filter-search {
  width: 100%;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.filter-search__title {
  margin-bottom: 20px;
  font-size: 18px;
}

.filter-search__wrapper {
  padding: 12px;
}

.filter-search__tool {
  margin-bottom: 12px;
}

.filter-search__tool:last-child {
  margin-bottom: 0;
}

.filter-search__tool.tool--grep {
  overflow: scroll;
  height: 320px;
}

.filter-search__tool-title {
  font-weight: 600;
  margin-bottom: 8px;
}

.filter-search__tool-title--second {
  font-weight: 500;
}

.filter-search__tool--files {
  margin-bottom: 12px;
}

.filter-search__tool--files:last-child {
  margin-bottom: 0;
}

.filter-search__tool-repositories {
  margin-top: 12px;
}

.filter-search__tool-repository {
  margin-bottom: 8px;
}

.filter-search__tool--files-input {
  margin-top: 4px;
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

.filter-search__reset {
  margin: 12px;
}

.filter-search__reset--wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
}

.filter-search__reset-btn {
  width: 100%;
  padding: 8px 0;
  background-color: var(--color-danger);
  color: var(--vt-c-white);
  border-radius: 4px;
}
</style>
