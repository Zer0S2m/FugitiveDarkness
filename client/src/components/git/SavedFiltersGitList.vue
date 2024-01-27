<template>
  <div
    class="saved-filters--container"
    v-if="items.length"
  >
    <ul class="saved-filters--items">
      <li
        @click="onSetGitFilterSearch(item, $event)"
        class="saved-filters--item"
        v-for="item in items"
        :key="item.id"
      >
        <SavedFiltersGitItem :item="item" />
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import type { IGitFilterSearch } from '@/types/gitFilterSearch';
import SavedFiltersGitItem from '@/components/git/SavedFiltersGitItem.vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';

const useGitRepositoryStore = useGitRepositoryState();

defineProps<{
  items: IGitFilterSearch[];
}>();

const onSetGitFilterSearch = async (filter: IGitFilterSearch, event): Promise<void> => {
  if (['saved-filter-item--wrapper', 'saved-filter-item--title'].includes(event.target.className)) {
    useGitRepositoryStore.resetAllFilters();
    useGitRepositoryStore.setAllFiltersForSearch(filter.filter);
  }
};
</script>

<style scoped>
.saved-filters--container {
  height: 80px;
  overflow-y: scroll;
}

.saved-filters--item {
  border: 1px solid;
  border-color: var(--color-border);
  border-radius: 4px;
  margin-bottom: 4px;
}
.saved-filters--item:hover {
  border-color: var(--color-secondary);
  cursor: pointer;
}
.saved-filters--item:last-child {
  margin-bottom: 0;
}
</style>
