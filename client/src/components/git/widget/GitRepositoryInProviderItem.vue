<template>
  <ContainerCardItem>
    <div class="wrapper">
      <h4 class="git-item__title">
        <a
          :href="item.link"
          target="_blank"
          >{{ item.project }}</a
        >
      </h4>
      <div
        class="tools"
        v-if="isLoadRepo"
      >
        <CheckIcon class="check" />
      </div>
    </div>
  </ContainerCardItem>
</template>

<script setup lang="ts">
import CheckIcon from '@/assets/check-icon.svg';
import ContainerCardItem from '@/components/common/ContainerCardItem.vue';
import type { IGitRepositoryInProvider } from '@/types/gitProvider';
import { computed } from 'vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';

const props = defineProps<{ item: IGitRepositoryInProvider }>();

const useGitRepositoryStore = useGitRepositoryState();

const isLoadRepo = computed(() => {
  return useGitRepositoryStore.checkExistsRepoBy_Host_Group_Project(
    props.item.host,
    props.item.group,
    props.item.project
  );
});
</script>

<style scoped>
.wrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.git-item__title {
  font-size: 18px;
  font-weight: 600;
}

.git-item__title > a {
  color: var(--vt-c-black);
}

.tools > .check {
  fill: var(--color-secondary);
}
</style>
