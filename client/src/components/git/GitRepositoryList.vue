<template>
  <ContainerCardList>
    <GitRepositoryItem
      v-for="item in items"
      :item="item"
    />
    <div class="git-item__add">
      <div class="git-item__add--wrapper">
        <button @click="openModalAddGitRepository">
          <IconAdd />
        </button>
      </div>
    </div>
  </ContainerCardList>
</template>

<script setup lang="ts">
import IconAdd from '@/assets/icon-add.svg';
import type { IGitRepository } from '@/types/gitRepository';
import GitRepositoryItem from '@/components/git/GitRepositoryItem.vue';
import ContainerCardList from '@/components/common/ContainerCardList.vue';

defineProps<{ items: IGitRepository[] }>();
const emit = defineEmits(['openModalAddGitRepository']);

const openModalAddGitRepository = () => {
  emit('openModalAddGitRepository');
};
</script>

<style scoped>
.git-item__add {
  border: 1px solid var(--color-border);
  width: 100%;
  margin: 4px;
  border-radius: 4px;
}

.git-item__add:hover {
  border: 1px solid var(--color-secondary);
}
.git-item__add:hover .git-item__add--wrapper > button > svg {
  fill: var(--color-secondary);
}

.git-item__add--wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.git-item__add--wrapper > button {
  width: 100%;
  height: 100%;
  padding: 16px 0;
}
</style>
