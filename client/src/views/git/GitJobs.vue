<script setup lang="ts">
import { HalfCircleSpinner } from 'epic-spinners';
import { useGitJobState } from '@/stores/useGitJobState';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import GitJobList from '@/components/git/container/GitJobList.vue';
import AddGitJobItemButton from '@/components/common/AddItemButton.vue';
import { useVfm } from 'vue-final-modal';

const useGitRepositoryStore = useGitRepositoryState();
const useGitJobStore = useGitJobState();
const useVfmStore = useVfm();

useGitRepositoryStore.loadGitRepositories();
useGitJobStore.loadGitJobs();

const openModalAddGitJobRepository = () => {
  useVfmStore.open('modalAddGitJob');
};
</script>

<template>
  <div class="git-jobs">
    <div class="git-jobs--title-container">
      <h2 class="title-container">Git jobs</h2>
      <AddGitJobItemButton
        @onClick="openModalAddGitJobRepository"
        class="git-job__add"
      />
    </div>
    <GitJobList
      :git-jobs="useGitJobStore.gitJobs"
      :git-repositories="useGitRepositoryStore.gitRepositories"
      v-if="!useGitJobStore.isGitJobsLoading"
    />
    <div
      class="loader-block"
      v-if="useGitJobStore.isGitJobsLoading"
    >
      <HalfCircleSpinner
        :animation-duration="1000"
        :size="60"
        color="var(--color-secondary)"
      />
    </div>
  </div>
</template>

<style scoped>
.git-jobs {
  overflow-y: auto;
  height: 100%;
}

.git-jobs--title-container {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.git-jobs--title-container .title-container {
  margin-bottom: 0;
  margin-right: 20px;
}
</style>
