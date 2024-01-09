<template>
  <div>
    <h2 class="title-container">Git providers</h2>
    <GitProviderList
      v-if="!useGitProviderStore.isLoading"
      :items="useGitProviderStore.gitProviders"
    />
    <div
      class="loader-block"
      v-if="useGitProviderStore.isLoading"
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
import { useGitProviderState } from '@/stores/useGitProviderState';
import { HalfCircleSpinner } from 'epic-spinners';
import GitProviderList from '@/components/git/GitProviderList.vue';

const useGitProviderStore = useGitProviderState();

useGitProviderStore.loadGitProviders();
</script>
