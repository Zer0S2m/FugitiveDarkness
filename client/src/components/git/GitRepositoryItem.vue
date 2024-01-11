<template>
  <ContainerCardItem>
    <div class="git-item__block-title">
      <h4 class="git-item__title">{{ item.group_ }}/{{ item.project }}</h4>
      <h5 class="git-item__host">{{ item.host }}</h5>
    </div>
    <div class="git-item__tools">
      <HalfCircleSpinner
        :animation-duration="1000"
        :size="22"
        v-if="!item.is_load"
        color="var(--color-secondary)"
      />
      <button
        class="git-item__delete"
        v-if="item.is_load"
        @click="deleteGitRepository"
      >
        <IconDelete class="git-item__delete-icon" />
      </button>
    </div>
  </ContainerCardItem>
</template>

<script setup lang="ts">
import IconDelete from '@/assets/icon-delete.svg';
import type { IGitRepository } from '@/types/gitRepository';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { HalfCircleSpinner } from 'epic-spinners';
import ContainerCardItem from '@/components/common/ContainerCardItem.vue';

const useGitRepositoryStore = useGitRepositoryState();
const props = defineProps<{ item: IGitRepository }>();

const deleteGitRepository = async () => {
  await useGitRepositoryStore.deleteGitRepository({
    group: props.item.group_,
    project: props.item.project
  });
};
</script>

<style scoped>
.git-item__title {
  font-size: 18px;
  font-weight: 600;
}

.git-item__host {
  margin-top: 12px;
}

.git-item__delete {
  width: 28px;
  height: 28px;
}
</style>
