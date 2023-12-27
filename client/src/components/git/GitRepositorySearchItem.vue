<template>
  <div>
    <h5 class="result-search-git-repo__title">{{ item.group }}/{{ item.project }}</h5>
    <GitRepositorySearchMatherList
      :matchers="item.found"
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
.result-search-git-repo__title {
  font-weight: 600;
}

.result-search-git-repo__matchers {
  margin-top: 12px;
}
</style>
