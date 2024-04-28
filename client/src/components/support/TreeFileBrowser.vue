<script setup lang="ts">
import type { ITree } from '@/types/tree';
import TreeFileBrowserNode from '@/components/support/TreeFileBrowserNode.vue';
import { computed } from 'vue';

const props = defineProps<{
  tree: ITree | undefined;
}>();

const emit = defineEmits<{
  (e: 'selectFileObject', fileObject: ITree): void;
}>();

const sortedByDirectoryAndFile = computed((): ITree => {
  if (!props.tree) {
    // @ts-ignore
    return;
  }

  const sortedTree: ITree = {
    path: props.tree.path,
    filename: props.tree.filename,
    isFile: props.tree.isFile,
    isDirectory: props.tree.isDirectory,
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
    @select-file-object="
      (fileObject: ITree) => {
        emit('selectFileObject', fileObject);
      }
    "
  />
</template>

<style scoped></style>
