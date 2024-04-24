<script setup lang="ts">
import ContainerCardItem from '@/components/common/ContainerCardItem.vue';
import { type IProject } from '@/types/project';
import router from '@/router';
import { useProjectState } from '@/stores/useProjectState';

const useProjectStore = useProjectState();

const props = defineProps<{
  item: IProject;
}>();

const viewProject = (): void => {
  useProjectStore.setActiveProject(props.item.id);
  router.push({ name: 'project-detail', params: { id: props.item.id } });
};
</script>

<template>
  <ContainerCardItem
    class="project-item"
    @click="viewProject"
  >
    <h6>{{ item.group }}/{{ item.project }}</h6>
  </ContainerCardItem>
</template>

<style scoped>
.project-item {
  cursor: pointer;
}

.project-item:hover {
  border: 1px solid var(--color-secondary);
}
</style>
