<template>
  <div class="matcher-notes">
    <h2 class="title-container">Notes on matches</h2>
    <div
      class="matcher-found--wrapper"
      v-if="!(useMatcherNoteStore.isLoading && useGitRepositoryStore.isLoading)"
    >
      <MatcherNoteList :items="useMatcherNoteStore.matcherNotes" />
    </div>
    <div
      class="loader-block"
      v-if="useMatcherNoteStore.isLoading || useGitRepositoryStore.isLoading"
    >
      <HalfCircleSpinner
        :animation-duration="1000"
        :size="60"
        color="var(--color-secondary)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { HalfCircleSpinner } from 'epic-spinners';
import { useMatcherNoteState } from '@/stores/useMatcherNoteState';
import { onMounted } from 'vue';
import MatcherNoteList from '@/components/matcherNote/MatcherNoteList.vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';

const useMatcherNoteStore = useMatcherNoteState();
const useGitRepositoryStore = useGitRepositoryState();

onMounted(async () => {
  await useGitRepositoryStore.loadGitRepositories();
  await useMatcherNoteStore.loadMatcherNotes();
});
</script>

<style scoped>
.matcher-notes {
  margin-bottom: 20px;
}

.matcher-found--wrapper {
  margin-top: 20px;
}
</style>
