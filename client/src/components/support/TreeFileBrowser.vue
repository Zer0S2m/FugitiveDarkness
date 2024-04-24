<script setup lang="ts">
import type { ITree } from '@/types/tree';
import TreeFileBrowserNode from '@/components/support/TreeFileBrowserNode.vue';
import { computed } from 'vue';

const props = defineProps<{
  tree: ITree | undefined;
}>();

const sortedByDirectoryAndFile = computed((): ITree => {
  if (!props.tree) {
    // @ts-ignore
    return;
  }

  const sortedTree: ITree = {
    // @ts-ignore
    path: props.tree.path,
    // @ts-ignore
    filename: props.tree.filename,
    // @ts-ignore
    isFile: props.tree.isFile,
    // @ts-ignore
    isDirectory: props.tree?.isDirectory,
    // @ts-ignore
    children: []
  };

  props.tree?.children.forEach((child: ITree): void => {
    if (child.isDirectory) {
      sortedTree.children.push(child);
    }
  });
  props.tree?.children.forEach((child: ITree): void => {
    if (child.isFile) {
      sortedTree.children.push(child);
    }
  });

  return sortedTree;
});
</script>

<template>
  <TreeFileBrowserNode
    v-if="tree"
    :tree="child"
    v-for="child in sortedByDirectoryAndFile.children"
  />
</template>

<style scoped>
.tree {
  cursor: pointer;
}
</style>
