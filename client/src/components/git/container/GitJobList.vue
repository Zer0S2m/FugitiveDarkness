<script setup lang="ts">
import type { IGitJob } from '@/types/gitJob';
import ContainerCardList from '@/components/common/ContainerCardList.vue';
import GitJobItem from '@/components/git/widget/GitJobItem.vue';
import type { IGitRepository } from '@/types/gitRepository';

const props = defineProps<{
  gitJobs: IGitJob[];
  gitRepositories: IGitRepository[];
}>();

const findGitRepositoryById = (idGitRepository: number): IGitRepository => {
  return props.gitRepositories.find((gitRepository: IGitRepository): boolean => {
    return gitRepository.id === idGitRepository;
  });
};
</script>

<template>
  <ContainerCardList>
    <GitJobItem
      :git-job="gitJob"
      :git-repository="findGitRepositoryById(gitJob.git_repository_id)"
      v-for="gitJob in gitJobs"
    />
  </ContainerCardList>
</template>

<style scoped></style>
