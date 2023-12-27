<template>
  <div>
    <h5 class="result-search-git-repo__title">
      <a
        :href="item.link"
        target="_blank"
        >{{ item.group }}/{{ item.project }}</a
      >
    </h5>
    <GitRepositorySearchMatherList
      :matchers="item.found"
      :group-repository="item.group"
      :project-repository="item.project"
      class="result-search-git-repo__matchers"
    />
  </div>
</template>

<script setup lang="ts">
import { type ISearchByGrepGitRepository } from '@/types/gitRepository';
import GitRepositorySearchMatherList from '@/components/git/GitRepositorySearchMatherList.vue';

defineProps<{
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
</style>
