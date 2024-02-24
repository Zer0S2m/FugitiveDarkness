<template>
  <VueFinalModal
    class="modal"
    content-class="modal-content modal-content--add-git-repository"
  >
    <h3 class="modal-title">{{ title }}</h3>
    <div class="modal-wrapper-form">
      <div
        class="modal-errors"
        v-if="!useGitRepositoryStore.stateFormAddGitRepositoryErrors.success"
      >
        <div class="modal-errors--wrapper">
          <p class="modal-errors-text">
            {{ useGitRepositoryStore.stateFormAddGitRepositoryErrors.msg }}
          </p>
        </div>
      </div>
      <Vueform v-model="dataForm">
        <TextElement
          name="remote"
          label="Remote"
        />
      </Vueform>
      <div class="modal-button__block">
        <button
          class="modal-add-button"
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
  remote: ''
};

const emit = defineEmits<{
  (e: 'confirm', dataForm: IInstallGitRepository): void;
}>();

const useGitRepositoryStore = useGitRepositoryState();
</script>
