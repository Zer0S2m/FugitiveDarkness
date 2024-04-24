<script setup lang="ts">
import type { ITree } from '@/types/tree';
import TreeFileBrowser from '@/components/support/TreeFileBrowser.vue';

defineProps<{
  tree: ITree | undefined;
}>();

const makeFolder = (item: ITree): void => {
  console.log(item);
};
</script>

<template>
  <div>
    <TreeFileBrowser
      :tree="tree"
      @make-folder="makeFolder"
    />
  </div>
</template>

<style scoped></style>
