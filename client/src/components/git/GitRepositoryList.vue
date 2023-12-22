<template>
  <div class="git-items">
    <GitRepositoryItem v-for="item in items" :item="item"/>
  </div>
</template>

<script setup lang="ts">
import type {IGitRepository} from "@/types/gitRepository";
import GitRepositoryItem from "@/components/git/GitRepositoryItem.vue";

defineProps<{ items: IGitRepository[] }>()
</script>

<style scoped>
.git-items {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  grid-column-gap: 8px;
  grid-row-gap: 2px;
}
</style>