<template>
  <div>
    <input class="search" placeholder="Search..." :value="useGitRepositoryStore.searchText">
  </div>
</template>

<script setup lang="ts">
import {useGitRepositoryState} from "@/stores/useGitRepositoryState";

const useGitRepositoryStore = useGitRepositoryState()
</script>

<style scoped>
.search {
  box-sizing: border-box;
  margin: 0;
  padding: 4px 12px;
  font-size: 14px;
  line-height: 1.5;
  list-style: none;
  position: relative;
  display: inline-block;
  width: 100%;
  min-width: 0;
  background-color: var(--color-background);
  border: 1px solid var(--color-border);
  border-radius: 4px;
}

.search:hover, .search:focus {
  border-color: var(--color-secondary);
}
</style>