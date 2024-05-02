<script setup lang="ts">
import { useVfm, VueFinalModal } from 'vue-final-modal';
import { GitJobType } from '@/enums/gitJob';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { useGitJobState } from '@/stores/useGitJobState';
import { computed } from 'vue';
import type { IGitJobCreate } from '@/types/gitJob';
import type { IGitRepository } from '@/types/gitRepository';

const useGitRepositoryStore = useGitRepositoryState();
const useGitJobStore = useGitJobState();
const useVfmStore = useVfm();

const dataForm: IGitJobCreate = {
  cron: '',
  gitRepositoryId: -1,
  type: ''
};

const gitRepositoriesItems = computed((): object => {
  const gitRepositories: object = {};

  useGitRepositoryStore.gitRepositories
    .filter((gitRepository: IGitRepository): boolean => {
      return !gitRepository.is_local;
    })
    .forEach((gitRepository: IGitRepository): void => {
      gitRepositories[gitRepository.id] = `${gitRepository.group_}/${gitRepository.project}`;
    });

  return gitRepositories;
});

const addGitJob = async (dataForm: IGitJobCreate): Promise<void> => {
  await useVfmStore.close('modalAddGitJob');

  await useGitJobStore.addGitJob(dataForm);
};
</script>

<template>
  <VueFinalModal
    modal-id="modalAddGitJob"
    class="modal"
    content-class="modal-content modal-content--add-git-job"
  >
    <h3 class="modal-title">Add git job</h3>
    <div class="modal-wrapper-form">
      <Vueform
        @submit="addGitJob(dataForm)"
        v-model="dataForm"
      >
        <TextElement
          name="cron"
          label="Cron expression"
          :rules="['required']"
        />
        <RadiogroupElement
          name="type"
          :items="{
            PERMANENT: `${GitJobType.PERMANENT.toString()} - Continuous execution`,
            ONETIME_USE: `${GitJobType.ONETIME_USE.toString()} - One-time execution`
          }"
          label="Type"
          default="PERMANENT"
          :rules="['required']"
        />
        <SelectElement
          name="gitRepositoryId"
          label="Git repository"
          :native="false"
          :items="gitRepositoriesItems"
          :rules="['required']"
        />
      </Vueform>
      <div class="modal-button__block">
        <Button
          class="modal-add-button"
          @click="addGitJob(dataForm)"
        >
          Add
        </Button>
      </div>
    </div>
  </VueFinalModal>
</template>

<style scoped></style>
