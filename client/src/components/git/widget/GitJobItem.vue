<script setup lang="ts">
import type { IGitJob } from '@/types/gitJob';
import ContainerCardItem from '@/components/common/ContainerCardItem.vue';
import type { IGitRepository } from '@/types/gitRepository';
import IconDelete from '@/assets/icon-delete.svg';
import IconEdit from '@/assets/icon-note.svg';
import { useGitJobState } from '@/stores/useGitJobState';
import { useVfm } from 'vue-final-modal';

const useGitJobStore = useGitJobState();
const useVfmStore = useVfm();

const props = defineProps<{
  gitJob: IGitJob;
  gitRepository: IGitRepository;
}>();

const deleteGitJob = async (): Promise<void> => {
  await useGitJobStore.deleteGitJobById(props.gitJob.id);
};

const editGitJob = (): void => {
  useGitJobStore.setActiveGitJobId(props.gitJob.id);

  useVfmStore.open('modalEditGitJob');
};
</script>

<template>
  <ContainerCardItem>
    <div class="git-job--params">
      <div class="git-job__container-title">
        <h4 class="git-job__title">{{ gitRepository.group_ }}/{{ gitRepository.project }}</h4>
      </div>
      <div class="git-job__body">
        <h6 class="git-job-body--cron git-job-body--part">
          Cron - <span>{{ gitJob.cron }}</span>
        </h6>
        <h6 class="git-job-body--type git-job-body--part">
          Type - <span>{{ gitJob.type }}</span>
        </h6>
      </div>
    </div>
    <div class="git-job--tools">
      <button
        class="git-job--tool-delete"
        @click="deleteGitJob"
      >
        <IconDelete class="git-job__delete-icon" />
      </button>
      <button
        class="git-job--tool-edit"
        @click="editGitJob"
      >
        <IconEdit class="git-job__edit-icon" />
      </button>
    </div>
  </ContainerCardItem>
</template>

<style scoped>
.git-job__container-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.git-job__title {
  font-size: 18px;
  font-weight: 600;
}

.git-job__body {
  margin-top: 16px;
}

.git-job-body--cron {
  margin-bottom: 8px;
}
.git-job-body--part > span {
  font-weight: 600;
}

.git-job--tools {
  display: flex;
  flex-direction: column;
}

.git-job--tool-delete {
  margin-bottom: 4px;
}

.git-job--tool-delete > svg,
.git-job--tool-edit > svg {
  width: 24px;
  height: 24px;
}
</style>
