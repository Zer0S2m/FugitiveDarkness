<script setup lang="ts">
import type { ITree } from '@/types/tree';
import { computed, type Ref, ref } from 'vue';
import IconFolder from '@/assets/icon-folder.svg';
import IconFile from '@/assets/icon-file.svg';

const isOpen: Ref<boolean> = ref(false);

const props = defineProps<{
  tree: ITree;
}>();

const emit = defineEmits<{
  (e: 'selectFileObject', fileObject: ITree): void;
}>();

const openFolder = (): void => {
  emit('selectFileObject', props.tree);

  isOpen.value = !isOpen.value;
};

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
      @select-file-object="
        (fileObject: ITree) => {
          emit('selectFileObject', fileObject);
        }
      "
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
.tree:hover > .tree--child > span {
  color: var(--color-secondary);
}
.tree:hover > .tree--child > svg {
  fill: var(--color-secondary);
}

.tree__children {
  margin-left: 8px;
}

.tree--child {
  display: flex;
  align-items: center;
  margin-bottom: 2px;
}

.tree--child > svg {
  min-width: 18px;
  min-height: 18px;
  width: 18px;
  height: 18px;
}

.tree--child > span {
  margin-left: 8px;
}
</style>
