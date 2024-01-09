<template>
  <ContainerCardItem>
    <div>
      <div class="git-provider__block-title">
        <h4 class="git-providers__title">{{ item.target }}</h4>
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

defineProps<{ item: IGitProvider }>();
</script>

<style scoped>
.git-provider__block-title {
  display: flex;
  align-items: center;
}

.git-providers__title {
  font-size: 18px;
  font-weight: 600;
  margin-right: 12px;
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
</style>
