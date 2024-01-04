<template>
  <VueFinalModal
    class="modal"
    content-class="modal-content modal-content--add-git-repository"
  >
    <h3 class="modal-title">{{ title }}</h3>
    <div class="wrapper-form">
      <div
        class="errors"
        v-if="!useGitRepositoryStore.stateFormAddGitRepositoryErrors.success"
      >
        <div class="errors--wrapper">
          <p class="errors-text">{{ useGitRepositoryStore.stateFormAddGitRepositoryErrors.msg }}</p>
        </div>
      </div>
      <Vueform v-model="dataForm">
        <TextElement
          name="remote"
          label="Remote"
        />
        <TextElement
          name="group"
          label="Group"
        />
      </Vueform>
      <div class="button__block">
        <button
          class="add-button"
          @click="emit('confirm', dataForm)"
        >
          Add
        </button>
      </div>
    </div>
  </VueFinalModal>
</template>

<script setup lang="ts">
import { VueFinalModal } from 'vue-final-modal';
import type { IInstallGitRepository } from '@/types/gitRepository';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';

defineProps<{
  title?: string;
}>();

const dataForm: IInstallGitRepository = {
  remote: '',
  group: ''
};

const emit = defineEmits<{
  (e: 'confirm', dataForm: IInstallGitRepository): void;
}>();

const useGitRepositoryStore = useGitRepositoryState();
</script>

<style scoped>
.wrapper-form {
  margin-top: 12px;
}

.button__block {
  display: flex;
  justify-content: end;
  margin-top: 12px;
}

.add-button {
  padding: 6px 28px;
  border: 1px solid var(--color-secondary);
  border-radius: 4px;
}

.errors {
  border-radius: 4px;
  background-color: var(--vf-bg-danger);
}

.errors > .errors--wrapper {
  padding: 12px;
}

.errors-text {
  color: var(--vf-color-danger);
}
</style>
