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
import GitRepositoryItem from '@/components/git/widget/GitRepositoryItem.vue';
import ContainerCardList from '@/components/common/ContainerCardList.vue';

defineProps<{ items: IGitRepository[] }>();
</script>
