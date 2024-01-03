<template>
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
    onConfirm(dataForm: IInstallGitRepository) {
      close();
      useGitRepositoryStore.installingGitRepository(dataForm);
    }
  }
});

useGitRepositoryStore.loadGitRepositories();

const openModalAddGitRepository = () => {
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
</style>
