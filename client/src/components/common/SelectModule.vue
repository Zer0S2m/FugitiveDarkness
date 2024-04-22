<script setup lang="ts">
import type { Ref } from 'vue';
import { ref } from 'vue';
import IconGit from '@/assets/icon-git.svg';

const isOpen: Ref<boolean> = ref(false);
const options: string[] = ['git', 'project'];

defineProps<{
  selected: string;
}>();
const emit = defineEmits<{
  (e: 'movingModule', module: string): void;
}>();
</script>

<template>
  <div
    class="custom-select"
    :tabindex="0"
    @blur="isOpen = false"
  >
    <div
      class="selected"
      :class="{ open: isOpen }"
      @click="isOpen = !isOpen"
    >
      <div class="selected--wrapper">
        <IconGit v-if="selected === 'git'" />
        <span>{{ selected }}</span>
      </div>
    </div>
    <div
      class="items"
      :class="{ selectHide: !isOpen }"
    >
      <div
        v-for="(option, i) of options"
        :key="i"
        @click="
          isOpen = false;
          emit('movingModule', option);
        "
      >
        <IconGit v-if="option === 'git'" />
        <span>{{ option }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.custom-select {
  position: relative;
  width: 100%;
  min-width: 120px;
  height: 80%;
}

.custom-select .selected {
  width: 100%;
  height: 100%;
  border-radius: 4px;
  border: 1px solid var(--color-secondary);
}

.custom-select .selected--wrapper {
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.custom-select .selected .selected--wrapper > span {
  margin-left: 8px;
}

.custom-select .selected .selected--wrapper > svg {
  width: 20px;
  height: 20px;
}

.custom-select .open {
  border-radius: 4px 4px 0 0;
}

.custom-select .items {
  border-radius: 0 0 4px 4px;
  overflow: hidden;
  border-right: 1px solid var(--color-secondary);
  border-left: 1px solid var(--color-secondary);
  border-bottom: 1px solid var(--color-secondary);
  position: absolute;
  background-color: var(--color-white);
  width: 100%;
  z-index: 1;
}

.custom-select .items div {
  cursor: pointer;
  user-select: none;
  padding: 8px 4px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.custom-select .items div > svg {
  width: 20px;
  height: 20px;
  margin-right: 8px;
}

.selectHide {
  display: none;
}
</style>
