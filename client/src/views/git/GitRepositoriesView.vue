<template>
  <div class="git">
    <div class="git__local">
      <div class="git-title--container">
        <h2 class="title-container">Local repositories</h2>
        <AddGitItemButton
          @onClick="openModalAddGitRepository"
          class="git-item__add"
        />
      </div>
      <div>
        <div
          class="git-local--groups"
          v-if="!useGitRepositoryStore.isLoading"
          v-for="gitRepositories in collectedRepositoriesByGroup"
        >
          <h4 class="git-local-group--title">{{ gitRepositories[0] }}</h4>
          <GitRepositoryList :items="gitRepositories[1]" />
        </div>
      </div>
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
            <h3 class="git-from__providers-item--title">
              <a
                :href="getLinkForTargetProvider(gitProvider)"
                target="_blank"
                >{{ gitProvider.target }}</a
              >
            </h3>
            <IconGitlab v-if="gitProvider.type === GitProviderType.GITLAB" />
            <IconGithub v-if="gitProvider.type === GitProviderType.GITHUB" />
          </div>
          <GitRepositoryInProviderList
            :items="
              useGitProviderStore.getRepositoryInProviderByTypeAndTarget(
                gitProvider.type,
                gitProvider.target
              ).items
            "
            :error="
              useGitProviderStore.getRepositoryInProviderByTypeAndTarget(
                gitProvider.type,
                gitProvider.target
              ).error
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
  </div>
</template>

<script setup lang="ts">
import { HalfCircleSpinner } from 'epic-spinners';
import GitRepositoryList from '@/components/git/container/GitRepositoryList.vue';
import { useGitProviderState } from '@/stores/useGitProviderState';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { useModal } from 'vue-final-modal';
import ModalAddGitRepository from '@/components/git/modals/ModalAddGitRepository.vue';
import { type IGitRepository, type IInstallGitRepository } from '@/types/gitRepository';
import { GitProviderType } from '@/enums/gitProvider';
import IconGithub from '@/assets/github-mark.svg';
import IconGitlab from '@/assets/gitlab-mark.svg';
import GitRepositoryInProviderList from '@/components/git/container/GitRepositoryInProviderList.vue';
import { computed } from 'vue';
import AddGitItemButton from '@/components/common/AddItemButton.vue';
import { getLinkForTargetProvider } from '@/utils/gitProviders';

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

const collectedRepositoriesByGroup = computed((): Map<string, IGitRepository[]> => {
  const gitRepositories: Map<string, IGitRepository[]> = new Map<string, IGitRepository[]>();

  useGitRepositoryStore.gitRepositories.forEach((gitRepository: IGitRepository): void => {
    const key: string = `${gitRepository.group_} - ${gitRepository.host}`;
    if (gitRepositories.has(key)) {
      gitRepositories.get(key)?.push(gitRepository);
    } else {
      gitRepositories.set(key, [gitRepository]);
    }
  });

  return gitRepositories;
});

const openModalAddGitRepository = () => {
  useGitRepositoryStore.clearStateFormAddGitRepositoryErrors();
  open();
};
</script>

<style scoped>
.git {
  overflow-y: auto;
  height: 100%;
}

.git-title--container {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.git-title--container > h2 {
  margin-right: 20px;
}

.git-title--container .title-container {
  margin-bottom: 0;
}

.git-from__providers {
  padding-top: 20px;
}

.git-from__providers-item--title {
  font-size: 16px;
  margin-right: 8px;
}

.git-from__providers-item--title > a {
  font-weight: 500;
  text-decoration: none;
  color: var(--vt-c-black);
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
  margin-bottom: 12px;
}

.git-from__providers-item--list {
  margin-top: 12px;
}

.git-local--groups {
  margin-bottom: 16px;
}
.git-local--groups:last-child {
  margin-bottom: 0;
}

.git-local-group--title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 8px;
}
</style>
