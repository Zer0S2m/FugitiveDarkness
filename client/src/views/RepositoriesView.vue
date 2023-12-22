<template>
  <GitRepositoryList v-if="!useGitRepositoryStore.isLoading" :items="useGitRepositoryStore.gitRepositories"/>
  <div class="loader-block" v-if="useGitRepositoryStore.isLoading">
    <HalfCircleSpinner
        :animation-duration="1000"
        :size="60"
        color="var(--color-secondary)"
    />
  </div>
</template>

<script setup lang="ts">
import {HalfCircleSpinner} from 'epic-spinners'
import GitRepositoryList from "@/components/git/GitRepositoryList.vue";
import {useGitRepositoryState} from "@/stores/useGitRepositoryState";

const useGitRepositoryStore = useGitRepositoryState()
useGitRepositoryStore.loadGitRepositories()
</script>

<style scoped>
.loader-block {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>