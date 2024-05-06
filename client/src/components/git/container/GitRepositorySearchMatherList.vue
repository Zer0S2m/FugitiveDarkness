<template>
  <div>
    <GitRepositorySearchMatherItem
      :matcher="mather"
      :group-repository="groupRepository"
      :project-repository="projectRepository"
      v-for="mather in matchers"
      class="result-search-git-repo__matcher"
    />
  </div>
</template>

<script setup lang="ts">
import { type ISearchFoundByGrepGitRepository } from '@/types/gitRepository';
import GitRepositorySearchMatherItem from '@/components/git/widget/GitRepositorySearchMatherItem.vue';

defineProps<{
  matchers: ISearchFoundByGrepGitRepository[];
  groupRepository: string;
  projectRepository: string;
}>();
</script>

<style scoped>
.result-search-git-repo__matcher {
  margin-bottom: 12px;
}
.result-search-git-repo__matcher:last-child {
  margin-bottom: 0;
}
</style>
