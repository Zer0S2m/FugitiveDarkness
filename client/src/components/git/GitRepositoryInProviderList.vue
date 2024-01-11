<template>
  <ContainerCardList>
    <GitRepositoryInProviderItem
      :item="item"
      v-for="item in items"
    />
  </ContainerCardList>
</template>

<script setup lang="ts">
import ContainerCardList from '@/components/common/ContainerCardList.vue';
import type { IGitRepositoryInProvider } from '@/types/gitProvider';
import GitRepositoryInProviderItem from '@/components/git/GitRepositoryInProviderItem.vue';

defineProps<{ items: IGitRepositoryInProvider[] }>();
</script>
