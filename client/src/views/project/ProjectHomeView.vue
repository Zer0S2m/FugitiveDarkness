<script setup lang="ts">
import { HalfCircleSpinner } from 'epic-spinners';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import ProjectList from '@/components/project/container/ProjectList.vue';
import { conversionGitRepositoryToProject } from '@/utils/conversion';
import { onMounted } from 'vue';
import { useProjectState } from '@/stores/useProjectState';

const useGitRepositoryStore = useGitRepositoryState();
const useProjectStore = useProjectState();

useGitRepositoryStore.loadGitRepositories();

onMounted((): void => {
  useProjectStore.resetStatistics();
});
</script>

<template>
  <div v-if="!useGitRepositoryStore.isLoading">
    <ProjectList :items="conversionGitRepositoryToProject(useGitRepositoryStore.gitRepositories)" />
  </div>
  <div
    class="loader-block"
    v-if="useGitRepositoryStore.isLoading"
  >
    <HalfCircleSpinner
      :animation-duration="1000"
      :size="60"
      color="var(--color-secondary)"
    />
  </div>
</template>

<style scoped></style>
