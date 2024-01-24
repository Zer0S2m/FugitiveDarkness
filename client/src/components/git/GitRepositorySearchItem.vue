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
      :matchers="item.found.slice(0, resultSearchByGrepGitRepositoriesLines + 1)"
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
import { type ISearchByGrepGitRepository } from '@/types/gitRepository';
import GitRepositorySearchMatherList from '@/components/git/GitRepositorySearchMatherList.vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { computed, ref, type Ref } from 'vue';
import { ShowLinesResultSearch } from '@/enums/resultSearch';

const useGitRepositoryStore = useGitRepositoryState();

const resultSearchByGrepGitRepositoriesLines: Ref<number> = ref(
  ShowLinesResultSearch.DEFAULT.valueOf()
);

const isShowMoreMatchers = computed((): boolean => {
  return !(
    props.item.found.length === 0 ||
    props.item.found.length <= resultSearchByGrepGitRepositoriesLines.value
  );
});

const onClickShowMoreMatchers = (): void => {
  resultSearchByGrepGitRepositoriesLines.value += ShowLinesResultSearch.DEFAULT.valueOf();
};

const props = defineProps<{
  item: ISearchByGrepGitRepository;
}>();
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
