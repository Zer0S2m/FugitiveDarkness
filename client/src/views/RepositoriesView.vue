<template>
  <div class="git__local">
    <h2 class="git-local__title">Local repositories</h2>
    <GitRepositoryList
      v-if="!useGitRepositoryStore.isLoading"
      :items="useGitRepositoryStore.gitRepositories"
      @open-modal-add-git-repository="openModalAddGitRepository"
    />
    <div
      class="loader-block"
      v-if="useGitRepositoryStore.isLoading"
    >
      <HalfCircleSpinner
        :animation-duration="1000"
        :size="60"
        color="var(--color-secondary)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { HalfCircleSpinner } from 'epic-spinners';
import GitRepositoryList from '@/components/git/GitRepositoryList.vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { useModal } from 'vue-final-modal';
import ModalAddGitRepository from '@/components/git/modals/ModalAddGitRepository.vue';
import { type IInstallGitRepository } from '@/types/gitRepository';

const useGitRepositoryStore = useGitRepositoryState();
const { open, close } = useModal({
  component: ModalAddGitRepository,
  attrs: {
    title: 'Add git repository',
    async onConfirm(dataForm: IInstallGitRepository) {
      await useGitRepositoryStore.installingGitRepository(dataForm);

      if (useGitRepositoryStore.stateFormAddGitRepositoryErrors.success) {
        useGitRepositoryStore.clearStateFormAddGitRepositoryErrors();
        await close();
      }
    }
  }
});

useGitRepositoryStore.loadGitRepositories();

const openModalAddGitRepository = () => {
  useGitRepositoryStore.clearStateFormAddGitRepositoryErrors();
  open();
};
</script>

<style scoped>
.loader-block {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.git-local__title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
}
</style>
