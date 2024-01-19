<template>
  <div
    v-if="!isLoading"
    class="file-content"
  >
    <div class="matcher-found__result">
      <div class="matcher-found__result--wrapper">
        <div class="matcher-found__result--lines">
          <p v-for="content in fileContent">
            {{ content.lineNumber }}
          </p>
        </div>
        <div class="matcher-found__result--codes">
          <div class="code--one matcher-found__result--code">
            <Highlightjs
              class="code"
              :code="code"
              :language="useGitRepositoryStore.activeShowFile.language"
            />
          </div>
        </div>
      </div>
    </div>
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
import Highlightjs from '@lib/highlightjs';
import { HalfCircleSpinner } from 'epic-spinners';

const useGitRepositoryStore = useGitRepositoryState();
const fileContent: Ref<IResponseFileFromGitRepositoryContent[]> = ref([]);
const isLoading: Ref<boolean> = ref(true);

const loadData = async (): Promise<void> => {
  if (
    !useGitRepositoryStore.activeShowFile.file.project ||
    !useGitRepositoryStore.activeShowFile.file.group ||
    !useGitRepositoryStore.activeShowFile.file.file
  ) {
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

onMounted(async () => {
  await loadData();
});
</script>

<style scoped>
.file-content {
  margin-bottom: 20px;
}
</style>
