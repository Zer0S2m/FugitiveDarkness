<script setup lang="ts">
import { useVfm, VueFinalModal } from 'vue-final-modal';
import type { IGitJob, IGitJobEdit } from '@/types/gitJob';
import { useGitJobState } from '@/stores/useGitJobState';
import { watch, type Ref, ref } from 'vue';

const useGitJobStore = useGitJobState();
const useVfmStore = useVfm();

const dataForm: Ref<IGitJobEdit> = ref({
  cron: ''
});

watch(
  () => useGitJobStore.activeGitJobId,
  (id: number) => {
    const currentGitJob: IGitJob | undefined = useGitJobStore.getGitJobById(id);
    if (currentGitJob) {
      dataForm.value = {
        cron: currentGitJob.cron
      };
    }
  }
);

const editGitJob = async (data: IGitJobEdit): Promise<void> => {
  await useVfmStore.close('modalEditGitJob');

  dataForm.value = {
    cron: ''
  };

  await useGitJobStore.editGitJobById(useGitJobStore.activeGitJobId, data);
  useGitJobStore.setActiveGitJobId(-1);
};
</script>

<template>
  <VueFinalModal
    modal-id="modalEditGitJob"
    class="modal"
    content-class="modal-content modal-content--add-git-job"
  >
    <h3 class="modal-title">Edit git job</h3>
    <div class="modal-wrapper-form">
      <Vueform
        @submit="editGitJob(dataForm)"
        v-model="dataForm"
        :sync="true"
      >
        <TextElement
          name="cron"
          label="Cron expression"
          :rules="['required']"
        />
      </Vueform>
      <div class="modal-button__block">
        <Button
          class="modal-add-button"
          @click="editGitJob(dataForm)"
        >
          Edit
        </Button>
      </div>
    </div>
  </VueFinalModal>
</template>

<style scoped></style>
