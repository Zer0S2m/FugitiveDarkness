<template>
  <div v-if="useGitRepositoryStore.getIsActiveFilterByRepository(`${item.group}/${item.project}`)">
    <h5 class="result-search-git-repo__title">
      <a
        :href="item.link"
        target="_blank"
        >{{ item.group }}/{{ item.project }}</a
      >
    </h5>
    <GitRepositorySearchMatherList
      :matchers="foundMatchers"
      :group-repository="item.group"
      :project-repository="item.project"
      class="result-search-git-repo__matchers"
    />
    <div
      class="more-show"
      v-if="isShowMoreMatchers"
    >
      <button @click="onClickShowMoreMatchers">
        Show more +{{ ShowLinesResultSearch.DEFAULT.valueOf() }}
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  type ISearchByGrepGitRepository,
  type ISearchFoundByGrepGitRepository
} from '@/types/gitRepository';
import GitRepositorySearchMatherList from '@/components/git/container/GitRepositorySearchMatherList.vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { computed, onMounted, ref, type Ref, watch } from 'vue';
import { ShowLinesResultSearch } from '@/enums/resultSearch';

const useGitRepositoryStore = useGitRepositoryState();

const resultSearchByGrepGitRepositoriesLines: Ref<number> = ref(
  ShowLinesResultSearch.DEFAULT.valueOf()
);
const activeExtension: Ref<string[]> = ref([]);
const foundMatchers: Ref<ISearchFoundByGrepGitRepository[]> = ref([]);
const foundMatchersLength: Ref<number> = ref(0);

watch(
  [resultSearchByGrepGitRepositoriesLines, () => useGitRepositoryStore.filtersByExtensionFiles],
  // TODO: Crutch - needed to track items on the page (linked to - show plus 50)
  ([_, newFiltersByExtensionFiles]) => {
    const repository = `${props.item.group}/${props.item.project}`;
    if (newFiltersByExtensionFiles.has(repository)) {
      const _activeExtension: Array<string> = [];

      // @ts-ignore
      newFiltersByExtensionFiles
        .get(repository)
        .forEach((filter: { extension: string; isActive: boolean }): void => {
          if (filter.isActive) {
            _activeExtension.push(filter.extension);
          }
        });

      activeExtension.value = _activeExtension;

      const foundFilterByExtension: Array<ISearchFoundByGrepGitRepository> = [];

      props.item.found.forEach((found: ISearchFoundByGrepGitRepository): void => {
        if (activeExtension.value.includes(found.extension)) {
          foundFilterByExtension.push(found);
        }
      });

      foundMatchers.value = foundFilterByExtension.slice(
        0,
        resultSearchByGrepGitRepositoriesLines.value + 1
      );
      foundMatchersLength.value = foundFilterByExtension.length;
    }
  },
  {
    deep: true
  }
);

const isShowMoreMatchers = computed((): boolean => {
  return !(
    foundMatchersLength.value === 0 ||
    foundMatchersLength.value <= resultSearchByGrepGitRepositoriesLines.value
  );
});

const onClickShowMoreMatchers = (): void => {
  resultSearchByGrepGitRepositoriesLines.value += ShowLinesResultSearch.DEFAULT.valueOf();
};

const props = defineProps<{
  item: ISearchByGrepGitRepository;
}>();

onMounted((): void => {
  const _activeExtension: Array<string> = [];

  props.item.extensionFiles.forEach((extensionFile: string): void => {
    _activeExtension.push(extensionFile);
  });

  activeExtension.value = _activeExtension;

  foundMatchers.value = props.item.found.slice(0, resultSearchByGrepGitRepositoriesLines.value + 1);
  foundMatchersLength.value = props.item.found.length;
});
</script>

<style scoped>
.result-search-git-repo__title > a {
  color: var(--vt-c-black);
  font-weight: 600;
}

.result-search-git-repo__matchers {
  margin-top: 12px;
}

.more-show {
  align-items: center;
  justify-content: center;
  display: flex;
  margin-top: 12px;
}

.more-show > button {
  padding: 8px 28px;
  background-color: var(--vt-c-text-dark-3);
  border-radius: 4px;
}

.more-show > button:hover {
  background-color: var(--color-secondary);
  color: var(--vt-c-white);
}
</style>
