<template>
  <div>
    <GitRepositorySearchMatherItem
      :matcher="mather"
      v-for="mather in matchers"
      class="result-search-git-repo__matcher"
    />
  </div>
</template>

<script setup lang="ts">
import { type ISearchFoundByGrepGitRepository } from '@/types/gitRepository';
import GitRepositorySearchMatherItem from '@/components/git/GitRepositorySearchMatherItem.vue';

defineProps<{
  matchers: ISearchFoundByGrepGitRepository[];
}>();
</script>

<style scoped>
.result-search-git-repo__matcher {
  margin-bottom: 12px;
}
</style>
