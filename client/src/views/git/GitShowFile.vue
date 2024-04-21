<template>
  <div
    v-if="!isLoading"
    class="file-content"
  >
    <Code
      :code="code"
      :language="useGitRepositoryStore.activeShowFile.language"
      :line-numbers="lineNumbers"
    />
  </div>
  <div
    class="loader-block"
    v-if="isLoading"
  >
    <HalfCircleSpinner
      :animation-duration="1000"
      :size="60"
      color="var(--color-secondary)"
    />
  </div>
</template>

<script setup lang="ts">
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import api from '@/services/api';
import { computed, onMounted, type Ref, ref } from 'vue';
import type { IResponseFileFromGitRepositoryContent } from '@/types/gitRepository';
import type { AxiosResponse } from 'axios';
import type { IResponseFileFromGitRepository } from '@/types/gitRepository';
import Code from '@/components/common/Code.vue';
import { HalfCircleSpinner } from 'epic-spinners';
import { useRoute } from 'vue-router';

const useGitRepositoryStore = useGitRepositoryState();
const vueRouteStore = useRoute();
const fileContent: Ref<IResponseFileFromGitRepositoryContent[]> = ref([]);
const isLoading: Ref<boolean> = ref(true);

const loadData = async (): Promise<void> => {
  let isDataSet: boolean = false;
  if (
    !useGitRepositoryStore.activeShowFile.file.project ||
    !useGitRepositoryStore.activeShowFile.file.group ||
    !useGitRepositoryStore.activeShowFile.file.file
  ) {
    isDataSet = false;
  }

  if (!isDataSet) {
    if (
      vueRouteStore.query['f'] &&
      vueRouteStore.query['group'] &&
      vueRouteStore.query['project'] &&
      vueRouteStore.query['e']
    ) {
      isDataSet = true;
      useGitRepositoryStore.setActiveShowFile(
        {
          group: vueRouteStore.query['group'] as string,
          project: vueRouteStore.query['project'] as string,
          file: vueRouteStore.query['f'] as string
        },
        vueRouteStore.query['e'] as string
      );
    } else {
      isDataSet = false;
    }
  }

  if (!isDataSet) {
    return;
  }

  isLoading.value = true;

  const resultGetFile: AxiosResponse<IResponseFileFromGitRepository> = await api.getFileContent(
    useGitRepositoryStore.activeShowFile.file
  );
  if (resultGetFile.data.success) {
    fileContent.value = resultGetFile.data.content;
  }

  isLoading.value = false;
};

const code = computed((): string => {
  let codeStr = '';
  fileContent.value.forEach((fileContentItem): void => {
    codeStr += `${fileContentItem.line}\n`;
  });
  return codeStr;
});

const lineNumbers = computed((): number[] => {
  const lineNumber: number[] = [];
  fileContent.value.forEach((obj) => {
    lineNumber.push(obj.lineNumber);
  });
  return lineNumber;
});

onMounted(async () => {
  await loadData();
});
</script>

<style scoped>
.file-content {
  margin-bottom: 20px;
}
</style>
