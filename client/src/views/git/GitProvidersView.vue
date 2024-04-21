<template>
  <div>
    <h2 class="title-container">Git providers</h2>
    <GitProviderList
      v-if="!useGitProviderStore.isLoading"
      :items="useGitProviderStore.gitProviders"
      @openModalAddGitProvider="openModalAddGitProvider"
    />
    <div
      class="loader-block"
      v-if="useGitProviderStore.isLoading"
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
import { useGitProviderState } from '@/stores/useGitProviderState';
import { HalfCircleSpinner } from 'epic-spinners';
import GitProviderList from '@/components/git/container/GitProviderList.vue';
import { useModal } from 'vue-final-modal';
import ModalAddGitProvider from '@/components/git/modals/ModalAddGitProvider.vue';
import type { IInstallGitProvider } from '@/types/gitProvider';

const useGitProviderStore = useGitProviderState();
const { open, close } = useModal({
  component: ModalAddGitProvider,
  attrs: {
    title: 'Add git provider',
    async onConfirm(dataForm: IInstallGitProvider) {
      await useGitProviderStore.installingGitProvider(dataForm);

      if (useGitProviderStore.stateFormAddGitProviderErrors.success) {
        useGitProviderStore.clearStateFormAddGitProviderErrors();
        await close();
      }
    }
  }
});

useGitProviderStore.loadGitProviders();

const openModalAddGitProvider = () => {
  useGitProviderStore.clearStateFormAddGitProviderErrors();
  open();
};
</script>
