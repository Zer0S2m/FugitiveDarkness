<template>
  <div>
    <GitRepositoryList :items="items"/>
  </div>
</template>

<script setup lang="ts">
import GitRepositoryList from "@/components/git/GitRepositoryList.vue";
import type {GitRepository} from "@/types/gitRepository";

const items: GitRepository[] = [
  {
    id: 1,
    group_: 'test',
    project: 'test',
    host: 'github.com',
    create_at: '2023-12-16T11:23:53.576425',
    is_load: true
  },
  {
    id: 2,
    group_: 'test_2',
    project: 'test_2',
    host: 'github.com',
    create_at: '2023-12-16T11:23:53.576425',
    is_load: true
  }
]
</script>