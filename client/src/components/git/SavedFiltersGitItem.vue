<template>
  <div class="saved-filter-item--wrapper">
    <h6 class="saved-filter-item--title">
      {{ item.title }}
    </h6>
    <div class="saved-filter-item--tools">
      <button
        class="delete"
        @click="onDeleteGitFilterSearch(item.id)"
      >
        <IconDelete class="delete-icon" />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import IconDelete from '@/assets/icon-delete.svg';
import type { IGitFilterSearch } from '@/types/gitFilterSearch';
import { useGitFilterSearchState } from '@/stores/useGitFilterSearchState';

const useGitFilterSearchStore = useGitFilterSearchState();

defineProps<{
  item: IGitFilterSearch;
}>();

const onDeleteGitFilterSearch = async (id: number): Promise<void> => {
  await useGitFilterSearchStore.deleteGitFilterSearch(id);
};
</script>

<style scoped>
.saved-filter-item--wrapper {
  padding: 6px 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.saved-filter-item--title {
  font-size: 15px;
}

.saved-filter-item--tools {
  margin-left: 8px;
}

.saved-filter-item--tools > .delete > svg {
  width: 20px;
  height: 20px;
}
</style>
