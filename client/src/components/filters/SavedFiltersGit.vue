<template>
  <div class="saved-filters">
    <div class="saved-filters--wrapper">
      <h5 class="saved-filters__title">Saved filters</h5>
      <div
        class="loader-block"
        v-if="useGitFilterSearchStore.isLoading"
      >
        <HalfCircleSpinner
          :animation-duration="1000"
          :size="24"
          color="var(--color-secondary)"
        />
      </div>
      <GitSavedFiltersList
        class="saved-filters--block"
        v-if="useGitFilterSearchStore.isLoad"
        :items="useGitFilterSearchStore.gitGitFiltersSearch"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useGitFilterSearchState } from '@/stores/useGitFilterSearchState';
import GitSavedFiltersList from '@/components/git/container/GitSavedFiltersList.vue';
import { HalfCircleSpinner } from 'epic-spinners';

const useGitFilterSearchStore = useGitFilterSearchState();

onMounted(async () => {
  await useGitFilterSearchStore.loadGitFilterSearch();
});
</script>

<style scoped>
.saved-filters {
  border: 1px solid var(--color-border);
  border-radius: 4px;
}

.saved-filters__title {
  font-size: 18px;
}

.saved-filters--wrapper {
  padding: 12px;
}

.saved-filters--block {
  margin-top: 20px;
}
</style>
