<template>
  <ContainerCardItem>
    <div class="git-providers__wrapper">
      <div class="git-providers__base">
        <div class="git-provider__block-title">
          <h4 class="git-providers__title">
            <a
              :href="getLinkForTargetProvider(item)"
              target="_blank"
              >{{ item.target }}</a
            >
          </h4>
          <IconGitlab v-if="item.type === GitProviderType.GITLAB" />
          <IconGithub v-if="item.type === GitProviderType.GITHUB" />
        </div>
        <div class="git-provider__info">
          <div class="git-provider__user">
            <div class="git-provider__block--wrapper">
              <IconUser />
              <span>User - {{ item.is_user ? 'yes' : 'no' }}</span>
            </div>
          </div>
          <div class="git-provider__org">
            <div class="git-provider__block--wrapper">
              <IconOrganization />
              <span>Organization - {{ item.is_org ? 'yes' : 'no' }}</span>
            </div>
          </div>
          <div class="git-provider__date_add">
            <div class="git-provider__block--wrapper">
              <div style="min-width: 20px">
                <IconDateAdd />
              </div>
              <span>Date of add - {{ convertDate }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="git-providers__tools">
        <button
          class="git-provider-item--delete"
          @click="deleteGitProvider"
        >
          <IconDelete class="git-item__delete-icon" />
        </button>
        <button
          class="git-provider-item--download"
          @click="loadGitRepo"
        >
          <IconDownload />
        </button>
      </div>
    </div>
  </ContainerCardItem>
</template>

<script setup lang="ts">
import type { IGitProvider } from '@/types/gitProvider';
import ContainerCardItem from '@/components/common/ContainerCardItem.vue';
import IconGitlab from '@/assets/gitlab-mark.svg';
import IconGithub from '@/assets/github-mark.svg';
import IconUser from '@/assets/icon-user.svg';
import IconOrganization from '@/assets/icon-organization.svg';
import { GitProviderType } from '@/enums/gitProvider';
import IconDelete from '@/assets/icon-delete.svg';
import IconDownload from '@/assets/icon-download.svg';
import IconDateAdd from '@/assets/icon-date-add.svg';
import { useGitProviderState } from '@/stores/useGitProviderState';
import { getLinkForTargetProvider } from '@/utils/gitProviders';
import { computed } from 'vue';

const props = defineProps<{ item: IGitProvider }>();

const useGitProviderStore = useGitProviderState();

const deleteGitProvider = async () => {
  await useGitProviderStore.deleteGitProvider({
    type: props.item.type,
    target: props.item.target
  });
};

const loadGitRepo = async () => {
  await useGitProviderStore.loadRepoFromRemoteHost({
    type: props.item.type,
    target: props.item.target
  });
};

const convertDate = computed((): string => {
  return new Date(props.item.created_at).toUTCString();
});
</script>

<style scoped>
.git-providers__wrapper {
  display: flex;
  width: 100%;
}

.git-providers__base {
  width: 100%;
}

.git-provider__block-title {
  display: flex;
  align-items: center;
}

.git-providers__title {
  font-size: 18px;
  margin-right: 12px;
}

.git-providers__title > a {
  font-weight: 600;
  text-decoration: none;
  color: var(--vt-c-black);
}

.git-provider__info {
  margin-top: 12px;
}

.git-provider__user {
  margin-bottom: 4px;
}

.git-provider__block--wrapper {
  display: flex;
  align-items: center;
}

.git-provider__block--wrapper > span {
  margin-left: 8px;
}

.git-providers__tools {
  display: flex;
  flex-direction: column;
  align-content: center;
}

.git-provider-item--download {
  margin-top: 12px;
}

.git-provider__date_add {
  margin-top: 2px;
}
</style>
