<template>
  <ContainerCardItem>
    <div class="git-item__block-title">
      <h4 class="git-item__title">{{ item.group_ }}/{{ item.project }}</h4>
      <h5 class="git-item__host">{{ item.host }}</h5>
      <div class="git-item__date-add">
        <div style="min-width: 20px; margin-right: 4px">
          <IconDateAdd />
        </div>
        <span>Date of add - {{ convertDate }}</span>
      </div>
    </div>
    <div class="git-item__tools">
      <div>
        <HalfCircleSpinner
          :animation-duration="1000"
          :size="22"
          v-if="!item.is_load"
          color="var(--color-secondary)"
        />
        <div class="git-item__tools--wrapper">
          <button
            class="git-item__delete"
            v-if="item.is_load"
            @click="deleteGitRepository"
          >
            <IconDelete class="git-item__delete-icon" />
          </button>
          <button
            class="git-item__update"
            v-if="item.is_load"
            @click="updateGitRepository"
          >
            <IconUpdate
              v-if="!item.is_local"
              class="git-item__update-icon"
            />
          </button>
          <button
            class="git-item__settings"
            v-if="item.is_load"
            @click="onClickOpenSettingsGitRepository"
          >
            <IconSettings />
          </button>
        </div>
      </div>
    </div>
  </ContainerCardItem>
</template>

<script setup lang="ts">
import IconDelete from '@/assets/icon-delete.svg';
import IconSettings from '@/assets/icon-settings.svg';
import IconUpdate from '@/assets/icon-update.svg';
import type { IGitRepository } from '@/types/gitRepository';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { HalfCircleSpinner } from 'epic-spinners';
import ContainerCardItem from '@/components/common/ContainerCardItem.vue';
import IconDateAdd from '@/assets/icon-date-add.svg';
import { computed } from 'vue';
import { useVfm } from 'vue-final-modal';

const useGitRepositoryStore = useGitRepositoryState();
const useVfmStore = useVfm();
const props = defineProps<{ item: IGitRepository }>();

const deleteGitRepository = async (): Promise<void> => {
  await useGitRepositoryStore.deleteGitRepository({
    group: props.item.group_,
    project: props.item.project
  });
};

const updateGitRepository = async (): Promise<void> => {
  await useGitRepositoryStore.updateGitRepositoryOperationFetch({
    group: props.item.group_,
    project: props.item.project
  });
};

const onClickOpenSettingsGitRepository = (): void => {
  useGitRepositoryStore.setActiveSelectGitRepositorySettings(props.item);
  useVfmStore.open('modalSettingsGitFilterSearch');
};

const convertDate = computed((): string => {
  return new Date(props.item.created_at).toUTCString();
});
</script>

<style scoped>
.git-item__block-title {
  padding-right: 12px;
}

.git-item__title {
  font-size: 18px;
  font-weight: 600;
}

.git-item__host {
  margin-top: 12px;
}

.git-item__tools--wrapper {
  display: flex;
  flex-direction: column;
}

.git-item__delete,
.git-item__settings {
  margin-bottom: 4px;
}

.git-item__delete > svg,
.git-item__update > svg,
.git-item__settings > svg {
  width: 20px;
  height: 20px;
}

.git-item__date-add {
  display: flex;
  margin-top: 12px;
}
</style>
