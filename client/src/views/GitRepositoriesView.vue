<template>
  <div class="git__local">
    <h2 class="title-container">Local repositories</h2>
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
  <div class="git-from__providers">
    <h2 class="title-container">Repositories from providers</h2>
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
    <ul
      v-if="!useGitProviderStore.isLoading"
      class="git-from__providers-items"
    >
      <li
        class="git-from__providers-item"
        v-for="gitProvider in useGitProviderStore.gitProviders"
      >
        <div class="git-from__providers-item__container">
          <h3 class="git-from__providers-item--title">{{ gitProvider.target }}</h3>
          <IconGitlab v-if="gitProvider.type === GitProviderType.GITLAB" />
          <IconGithub v-if="gitProvider.type === GitProviderType.GITHUB" />
        </div>
        <GitRepositoryInProviderList
          :items="
            useGitProviderStore.getRepositoryInProviderByTypeAndTarget(
              gitProvider.type,
              gitProvider.target
            )
          "
          v-if="
            !useGitProviderStore.getIsLoadingRepositoryInProviderByTypeAndTarget(
              gitProvider.type,
              gitProvider.target
            )
          "
          class="git-from__providers-item--list"
        />
        <div
          class="loader-block"
          v-if="
            useGitProviderStore.getIsLoadingRepositoryInProviderByTypeAndTarget(
              gitProvider.type,
              gitProvider.target
            )
          "
        >
          <HalfCircleSpinner
            :animation-duration="1000"
            :size="60"
            color="var(--color-secondary)"
          />
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { HalfCircleSpinner } from 'epic-spinners';
import GitRepositoryList from '@/components/git/GitRepositoryList.vue';
import { useGitProviderState } from '@/stores/useGitProviderState';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { useModal } from 'vue-final-modal';
import ModalAddGitRepository from '@/components/git/modals/ModalAddGitRepository.vue';
import { type IInstallGitRepository } from '@/types/gitRepository';
import { GitProviderType } from '@/enums/gitProvider';
import IconGithub from '@/assets/github-mark.svg';
import IconGitlab from '@/assets/gitlab-mark.svg';
import GitRepositoryInProviderList from '@/components/git/GitRepositoryInProviderList.vue';

const useGitRepositoryStore = useGitRepositoryState();
const useGitProviderStore = useGitProviderState();
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
useGitProviderStore.loadGitProviders();

const openModalAddGitRepository = () => {
  useGitRepositoryStore.clearStateFormAddGitRepositoryErrors();
  open();
};
</script>

<style scoped>
.git__local {
  margin-bottom: 28px;
}

.git-from__providers {
  padding-bottom: 20px;
}

.git-from__providers-item--title {
  font-size: 16px;
  font-weight: 500;
  margin-right: 8px;
}

.git-from__providers-item {
  margin-bottom: 20px;
}
.git-from__providers-item:last-child {
  margin-bottom: 0;
}

.git-from__providers-item__container {
  display: flex;
  align-items: center;
}

.git-from__providers-item--list {
  margin-top: 12px;
}
</style>
