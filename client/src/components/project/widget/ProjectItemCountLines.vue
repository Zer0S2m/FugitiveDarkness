<script setup lang="ts">
import { HalfCircleSpinner } from 'epic-spinners';
import type { IProjectFileInfoCountFile } from '@/types/project';

defineProps<{
  isLoading: boolean;
  codeLines: IProjectFileInfoCountFile[];
}>();
</script>

<template>
  <div class="lines">
    <div class="lines--wrapper">
      <h4 class="lines--title">Code lines</h4>
      <div
        class="lines--loader"
        v-if="isLoading"
      >
        <HalfCircleSpinner
          :animation-duration="1000"
          :size="32"
          color="var(--color-secondary)"
        />
      </div>
      <ul
        class="lines--items"
        v-if="!isLoading"
      >
        <li
          class="lines--item"
          v-for="codeLine in codeLines"
        >
          <h5 class="lines-item--title">
            <span>{{ codeLine.path }}</span>
            <span>{{ codeLine.countLines }}</span>
          </h5>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.lines {
  border-radius: 4px;
  border: 1px solid var(--color-secondary);
}

.lines--wrapper {
  padding: 8px;
  overflow: hidden;
}

.lines--title {
  font-size: 18px;
  font-weight: 600;
}

.lines--loader {
  margin-top: 8px;
  display: flex;
  justify-content: center;
}

.lines--items {
  margin-top: 8px;
  height: 202px;
  overflow-y: auto;
}

.lines--item {
  margin-bottom: 6px;
}
.lines--item:last-child {
  margin-bottom: 0;
}

.lines-item--title > span:first-child {
  margin-right: 12px;
}
.lines-item--title > span:last-child {
  font-weight: 600;
}
</style>
