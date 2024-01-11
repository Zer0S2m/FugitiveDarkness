<template>
  <ContainerCardList>
    <GitRepositoryItem
      v-for="item in items"
      :item="item"
    />
    <AddGitItemButton
      @onClick="openModalAddGitRepository"
      class="git-item__add"
    />
  </ContainerCardList>
</template>

<script setup lang="ts">
import type { IGitRepository } from '@/types/gitRepository';
import GitRepositoryItem from '@/components/git/GitRepositoryItem.vue';
import ContainerCardList from '@/components/common/ContainerCardList.vue';
import AddGitItemButton from '@/components/common/AddGitItemButton.vue';

defineProps<{ items: IGitRepository[] }>();
const emit = defineEmits(['openModalAddGitRepository']);

const openModalAddGitRepository = () => {
  emit('openModalAddGitRepository');
};
</script>

<style scoped>
.git-item__add {
  margin: 4px;
}
</style>
