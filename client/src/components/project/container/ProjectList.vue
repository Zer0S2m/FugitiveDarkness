<script setup lang="ts">
import ContainerCardList from '@/components/common/ContainerCardList.vue';
import type { IProject } from '@/types/project';
import ProjectItem from '@/components/project/widget/ProjectItem.vue';

defineProps<{
  items: IProject[];
}>();
</script>

<template>
  <ContainerCardList>
    <ProjectItem
      v-for="item in items"
      :item="item"
    />
  </ContainerCardList>
</template>

<style scoped></style>
