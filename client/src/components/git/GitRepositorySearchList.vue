<template>
  <GitRepositorySearchItem
    :item="item"
    v-for="item in items"
    class="search-git-repo"
  />
</template>

<script setup lang="ts">
import { type ISearchByGrepGitRepository } from '@/types/gitRepository';
import GitRepositorySearchItem from '@/components/git/GitRepositorySearchItem.vue';

defineProps<{
  items: ISearchByGrepGitRepository[];
}>();
</script>

<style scoped>
.search-git-repo {
  margin-bottom: 20px;
}
</style>
