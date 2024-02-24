<template>
  <ContainerCardList>
    <GitRepositoryItem
      v-for="item in items"
      :item="item"
    />
  </ContainerCardList>
</template>

<script setup lang="ts">
import type { IGitRepository } from '@/types/gitRepository';
import GitRepositoryItem from '@/components/git/GitRepositoryItem.vue';
import ContainerCardList from '@/components/common/ContainerCardList.vue';

defineProps<{ items: IGitRepository[] }>();
</script>

<style scoped>
.git-item__add {
  margin: 4px;
}
</style>
