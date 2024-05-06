<script setup lang="ts">
import { HalfCircleSpinner } from 'epic-spinners';
import type { IProjectFileCommit } from '@/types/project';
import type { IError } from '@/types/api';
import AlertError from '@/components/common/AlertError.vue';

defineProps<{
  isLoading: boolean;
  title: string;
  commit: IProjectFileCommit | null;
  error: IError | null;
}>();
</script>

<template>
  <div class="commit">
    <div class="commit--wrapper">
      <h4 class="commit--title">{{ title }}</h4>
      <div
        class="commit--loader"
        v-if="isLoading"
      >
        <HalfCircleSpinner
          :animation-duration="1000"
          :size="32"
          color="var(--color-secondary)"
        />
      </div>
      <div
        class="commit__body"
        v-if="commit && !error"
      >
        <h6 class="commit--part commit--hash">
          <span>Hash</span><span>{{ commit.commit }}</span>
        </h6>
        <h6 class="commit--part commit--author-name">
          <span>Author name</span><span>{{ commit.authorName }}</span>
        </h6>
        <h6 class="commit--part commit--author-email">
          <span>Author email</span><span>{{ commit.authorEmail }}</span>
        </h6>
        <h6 class="commit--part commit--body">
          <span>Body</span><span>{{ commit.bodyCommit }}</span>
        </h6>
      </div>
      <div
        class="commit__error"
        v-if="error"
      >
        <AlertError :error="error" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.commit {
  border-radius: 4px;
  border: 1px solid var(--color-secondary);
}

.commit--wrapper {
  padding: 8px;
  overflow: hidden;
}

.commit--title {
  font-size: 18px;
  font-weight: 600;
}

.commit--loader {
  margin-top: 8px;
  display: flex;
  justify-content: center;
}

.commit__body {
  margin-top: 8px;
}

.commit--part {
  margin-bottom: 6px;
  display: flex;
  justify-content: space-between;
  line-height: 1.3;
}
.commit--part:last-child {
  margin-bottom: 0;
}
.commit--part > span:first-child {
  width: 35%;
  max-width: 100%;
}
.commit--part > span:last-child {
  width: 64%;
  max-width: 100%;
  word-break: break-all;
}

.commit__error {
  display: flex;
  margin-top: 12px;
}
</style>
