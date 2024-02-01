<template>
  <div class="filter-search">
    <div class="filter-search__wrapper">
      <div class="filter-search-title--wrapper">
        <h5 class="filter-search__title">Filter</h5>
        <div class="filter-search--tools">
          <button
            @click="resetResult"
            class="reset"
          >
            Reset
          </button>
          <button
            @click="onClickSaveGitFilterSearch"
            class="save"
          >
            Save
          </button>
        </div>
      </div>
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
                  v-for="gitRepository in collectedGitRepositoriesByGroup"
                >
                  <Checkbox
                    v-if="gitRepository.is_load"
                    :meta="gitRepository"
                    :title="gitRepository.group_ + '/' + gitRepository.project"
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
        <div class="filter-search__tool--args">
          <h6 class="filter-search__tool-title">Arguments</h6>
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
          <div class="filter-search__tool--pattern-files">
            <h6 class="filter-search__tool-title--second">Include files by pattern</h6>
            <BaseInput
              class="filter-search__tool--pattern-files-input"
              :model-value="useGitRepositoryStore.filtersForSearch.filters.patternForIncludeFile"
              @update:model-value="useGitRepositoryStore.setPatternForIncludeFileForFilter"
            />
          </div>
          <div class="filter-search__tool--pattern-files">
            <h6 class="filter-search__tool-title--second">Exclude files by pattern</h6>
            <BaseInput
              class="filter-search__tool--pattern-files-input"
              :model-value="useGitRepositoryStore.filtersForSearch.filters.patternForExcludeFile"
              @update:model-value="useGitRepositoryStore.setPatternForExcludeFileForFilter"
            />
          </div>
          <div class="filter-search__tool--context">
            <h6 class="filter-search__tool-title--second">Show leading and trailing lines</h6>
            <ul class="filter-search__tool--context-items">
              <li class="filter-search__tool--context-item">
                <BaseInput
                  class="filter-search__tool--context-item-input"
                  type="number"
                  min="-1"
                  :model-value="String(useGitRepositoryStore.filtersForSearch.filters.context)"
                  @update:model-value="useGitRepositoryStore.setContextForFilter"
                />
                <span>Common</span>
              </li>
              <li class="filter-search__tool--context-item">
                <BaseInput
                  class="filter-search__tool--context-item-input"
                  type="number"
                  min="-1"
                  :model-value="
                    String(useGitRepositoryStore.filtersForSearch.filters.contextBefore)
                  "
                  @update:model-value="useGitRepositoryStore.setContextBeforeForFilter"
                />
                <span>Before</span>
              </li>
              <li class="filter-search__tool--context-item">
                <BaseInput
                  class="filter-search__tool--context-item-input"
                  type="number"
                  min="-1"
                  :model-value="String(useGitRepositoryStore.filtersForSearch.filters.contextAfter)"
                  @update:model-value="useGitRepositoryStore.setContextAfterForFilter"
                />
                <span>After</span>
              </li>
            </ul>
          </div>
          <div class="filter-search__tool--max_depth">
            <h6 class="filter-search__tool-title--second">Maximum search depth</h6>
            <BaseInput
              class="filter-search__tool--max_depth-input"
              type="number"
              min="-1"
              :model-value="String(useGitRepositoryStore.filtersForSearch.filters.maxDepth)"
              @update:model-value="useGitRepositoryStore.setMaxDepthForFilter"
            />
          </div>
          <div class="filter-search__tool--max_count">
            <h6 class="filter-search__tool-title--second">Maximum number of matches per file</h6>
            <BaseInput
              class="filter-search__tool--max_count-input"
              type="number"
              min="-1"
              :model-value="String(useGitRepositoryStore.filtersForSearch.filters.maxCount)"
              @update:model-value="useGitRepositoryStore.setMaxCountForFilter"
            />
          </div>
        </div>
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
import { computed, onMounted, type Ref, ref, watch } from 'vue';
import router from '@/router';
import BaseInput from '@/components/common/BaseInput.vue';
import { useGitFilterSearchState } from '@/stores/useGitFilterSearchState';
import { useVfm } from 'vue-final-modal';

const useVfmStore = useVfm();
const useGitFilterSearchStore = useGitFilterSearchState();
const useGitRepositoryStore = useGitRepositoryState();
useGitRepositoryStore.loadGitRepositories();

interface IGitRepositoryFileExtension extends IGitRepository {
  extensionFile: string;
}

const collectedGitRepositoriesByGroup = computed((): IGitRepository[] => {
  const repositoriesByGroup: Map<string, IGitRepository[]> = new Map<string, IGitRepository[]>();

  useGitRepositoryStore.gitRepositories.forEach((gitRepository: IGitRepository): void => {
    const key: string = gitRepository.host + gitRepository.group_;
    if (repositoriesByGroup.has(key)) {
      repositoriesByGroup.get(key)?.push(gitRepository);
    } else {
      repositoriesByGroup.set(key, [gitRepository]);
    }
  });

  const gitRepositories: IGitRepository[] = [];

  [...repositoriesByGroup.values()].forEach((gitRepositoriesAr: IGitRepository[]): void => {
    gitRepositories.push(...gitRepositoriesAr);
  });

  return gitRepositories;
});

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

const onClickSaveGitFilterSearch = (): void => {
  useGitFilterSearchStore.activeFilterForCreate = useGitRepositoryStore.filtersForSearch;

  useVfmStore.open('modalAddGitFilterSearch');
};

watch(
  () => useGitRepositoryStore.filtersForSearch.filters.includeExtensionFiles,
  (newValue: string[]) => {
    includeFilesExtension.value = newValue;
  }
);
watch(
  () => useGitRepositoryStore.filtersForSearch.filters.excludeExtensionFiles,
  (newValue: string[]) => {
    excludeFilesExtension.value = newValue;
  }
);

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

.filter-search-title--wrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;

  margin-bottom: 20px;
}

.filter-search--tools > .save,
.filter-search--tools > .reset {
  padding: 4px 20px;
  border-radius: 4px;
}

.filter-search--tools > .save {
  border: 1px solid var(--color-secondary);
}

.filter-search--tools > .reset {
  border: 1px solid var(--color-danger);
  margin-right: 4px;
}

.filter-search__title {
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

.filter-search__tool--args {
  height: 280px;
  overflow-y: scroll;
}

.filter-search__tool--files,
.filter-search__tool--pattern-files {
  margin-bottom: 12px;
}

.filter-search__tool--files:last-child,
.filter-search__tool--pattern-files:last-child {
  margin-bottom: 0;
}

.filter-search__tool-repositories {
  margin-top: 12px;
}

.filter-search__tool-repository {
  margin-bottom: 8px;
}

.filter-search__tool--files-input,
.filter-search__tool--pattern-files-input {
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

.filter-search__tool--context {
  margin-top: 8px;
}

.filter-search__tool--context-items {
  margin-top: 4px;
  display: flex;
  justify-content: space-between;
}

.filter-search__tool--context-item {
  max-width: 32%;
  width: 100%;
}

.filter-search__tool--context-item-input {
  margin-bottom: 4px;
}

.filter-search__tool--context-item > span {
  font-size: 14px;
}

.filter-search__tool--max_depth,
.filter-search__tool--max_count {
  margin-top: 8px;
  display: flex;
  flex-direction: column;
}

.filter-search__tool--max_depth-input,
.filter-search__tool--max_count-input {
  margin-top: 4px;
}

.filter-search__tool-repositories--loader {
  margin-top: 12px;
  display: flex;
  justify-content: center;
}
</style>
