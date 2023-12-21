<template>
  <div>
    <GitRepositoryList :items="useGitRepositoryStore.gitRepositories"/>
  </div>
</template>

<script setup lang="ts">
import GitRepositoryList from "@/components/git/GitRepositoryList.vue";
import {useGitRepositoryState} from "@/stores/useGitRepositoryState";

const useGitRepositoryStore = useGitRepositoryState()
await useGitRepositoryStore.loadGitRepositories()
</script>