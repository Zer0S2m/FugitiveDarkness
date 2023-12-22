<template>
  <GitRepositoryList v-if="!useGitRepositoryStore.isLoading" :items="useGitRepositoryStore.gitRepositories"/>
</template>

<script setup lang="ts">
import GitRepositoryList from "@/components/git/GitRepositoryList.vue";
import {useGitRepositoryState} from "@/stores/useGitRepositoryState";

const useGitRepositoryStore = useGitRepositoryState()
await useGitRepositoryStore.loadGitRepositories()
</script>