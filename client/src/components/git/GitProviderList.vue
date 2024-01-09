<template>
  <ContainerCardList>
    <GitProviderItem
      v-for="item in items"
      :item="item"
    />
  </ContainerCardList>
</template>

<script setup lang="ts">
import type { IGitProvider } from '@/types/gitProvider';
import ContainerCardList from '@/components/common/ContainerCardList.vue';
import GitProviderItem from '@/components/git/GitProviderItem.vue';

defineProps<{ items: IGitProvider[] }>();
</script>

<style scoped></style>
