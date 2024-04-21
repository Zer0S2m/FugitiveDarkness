<template>
  <div class="matcher-notes">
    <h2 class="title-container">Notes on matches</h2>
    <div
      class="matcher-found--wrapper"
      v-if="!(useGitMatcherNoteStore.isLoading && useGitRepositoryStore.isLoading)"
    >
      <MatcherNoteList :items="useGitMatcherNoteStore.matcherNotes" />
    </div>
    <div
      class="loader-block"
      v-if="useGitMatcherNoteStore.isLoading || useGitRepositoryStore.isLoading"
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
import { useGitMatcherNoteState } from '@/stores/useGitMatcherNoteState';
import { onMounted } from 'vue';
import MatcherNoteList from '@/components/git/container/GitMatcherNoteList.vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';

const useGitMatcherNoteStore = useGitMatcherNoteState();
const useGitRepositoryStore = useGitRepositoryState();

onMounted(async () => {
  await useGitRepositoryStore.loadGitRepositories();
  await useGitMatcherNoteStore.loadMatcherNotes();
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
