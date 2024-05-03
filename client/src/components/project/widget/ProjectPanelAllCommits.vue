<script setup lang="ts">
import type { IProjectFileCommit } from '@/types/project';
import { HalfCircleSpinner } from 'epic-spinners';
import IconArrowBottom from '@/assets/icon-arrow-bottom.svg';
import { type Ref, ref } from 'vue';
import AlertError from '@/components/common/AlertError.vue';

const openCommits: Ref<string[]> = ref([]);

defineProps<{
  isLoading: boolean;
  commits: IProjectFileCommit[] | null;
}>();

const openInfoCommit = (hash: string): void => {
  if (openCommits.value.includes(hash)) {
    const indexHash = openCommits.value.indexOf(hash);
    if (indexHash > -1) {
      openCommits.value.splice(indexHash, 1);
    }
  } else {
    openCommits.value.push(hash);
  }
};
</script>

<template>
  <div class="panel">
    <div class="panel--wrapper">
      <h4 class="panel--title">All commits</h4>
      <div
        class="panel--loader"
        v-if="isLoading"
      >
        <HalfCircleSpinner
          :animation-duration="1000"
          :size="32"
          color="var(--color-secondary)"
        />
      </div>
      <ul
        class="panel--commits"
        style="height: 202px"
        v-if="commits && commits.length"
      >
        <li
          class="panel--commit"
          :key="commit.commit"
          v-for="commit in commits"
          @click="openInfoCommit(commit.commit)"
        >
          <div class="commit--wrapper">
            <div class="commit-container-title">
              <h6 class="commit--title">{{ commit.commit }}</h6>
              <IconArrowBottom />
            </div>
            <div
              class="commit--body"
              v-if="openCommits.includes(commit.commit)"
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
          </div>
        </li>
      </ul>
      <div
        class="panel--error"
        v-if="(!commits || !commits.length) && !isLoading"
      >
        <AlertError
          :error="{
            type: 'NotFoundException',
            message: '[Not Found] The commit was not found'
          }"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.panel {
  border-radius: 4px;
  border: 1px solid var(--color-secondary);
}

.panel--wrapper {
  padding: 8px;
}

.panel--title {
  font-size: 18px;
  font-weight: 600;
}

.panel--loader {
  justify-content: center;
  display: flex;
}

.panel--commits {
  margin-top: 8px;
  overflow-y: auto;
}

.panel--commit {
  margin-bottom: 8px;
  cursor: pointer;
}
.panel--commit:last-child {
  margin-bottom: 0;
}
.panel--commit:hover .commit-container-title > svg {
  fill: var(--color-secondary);
}
.panel--commit:hover .commit--title {
  color: var(--color-secondary);
}

.commit--wrapper {
  padding: 8px;
}

.commit-container-title {
  display: flex;
  align-items: center;
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
  width: 20%;
  max-width: 100%;
}
.commit--part > span:last-child {
  width: 80%;
  max-width: 100%;
}

.panel--error {
  display: flex;
  margin-top: 12px;
}
</style>
