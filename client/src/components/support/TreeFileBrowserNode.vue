<script setup lang="ts">
import type { ITree } from '@/types/tree';
import { computed, type Ref, ref } from 'vue';
import IconFolder from '@/assets/icon-folder.svg';
import IconFile from '@/assets/icon-file.svg';

const isOpen: Ref<boolean> = ref(false);

const props = defineProps<{
  tree: ITree;
}>();

const openFolder = (): void => {
  isOpen.value = !isOpen.value;
};

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
  <div class="tree tree-root">
    <div
      class="tree--child"
      @click="openFolder"
    >
      <IconFolder v-if="tree.isDirectory" />
      <IconFile v-else-if="tree.isFile" />
      <span>{{ tree?.filename }}</span>
    </div>
    <TreeFileBrowserNode
      class="tree__children"
      v-if="sortedByDirectoryAndFile && isOpen"
      :tree="child"
      v-for="child in sortedByDirectoryAndFile.children"
    />
  </div>
</template>

<style scoped>
.tree {
  cursor: pointer;
}

.tree__children {
  margin-left: 4px;
}

.tree--child {
  display: flex;
  align-items: center;
}

.tree--child > svg {
  width: 18px;
  height: 18px;
}

.tree--child > span {
  margin-left: 8px;
}
</style>
