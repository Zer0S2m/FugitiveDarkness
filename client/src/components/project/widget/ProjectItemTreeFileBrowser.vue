<script setup lang="ts">
import type { ITree } from '@/types/tree';
import TreeFileBrowser from '@/components/support/TreeFileBrowser.vue';
import { useProjectState } from '@/stores/useProjectState';

const useProjectStore = useProjectState();

const selectFileObject = async (fileObject: ITree): Promise<void> => {
  useProjectStore.setActiveProjectFile(fileObject);

  useProjectStore.resetStatistics();

  await loadDataStatisticsByProjectFile();
};

const loadDataStatisticsByProjectFile = async (): Promise<void> => {
  await Promise.all([
    new Promise(() => {
      useProjectStore.loadProjectFileFirstCommit();
    }),
    new Promise(() => {
      useProjectStore.loadProjectFileLastCommit();
    }),
    new Promise(() => {
      useProjectStore.loadProjectFileAllCommit();
    }),
    new Promise(() => {
      useProjectStore.loadProjectFileTodos();
    }),
    new Promise(() => {
      useProjectStore.loadProjectFileCountLines();
    })
  ]);
};

defineProps<{
  tree: ITree | undefined;
}>();
</script>

<template>
  <div>
    <TreeFileBrowser
      :tree="tree"
      @select-file-object="selectFileObject"
    />
  </div>
</template>

<style scoped></style>
