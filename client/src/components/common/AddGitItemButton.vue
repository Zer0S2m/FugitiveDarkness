<template>
  <div class="git__add">
    <div class="git__add--wrapper">
      <button @click="emit('onClick')">
        <IconAdd />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import IconAdd from '@/assets/icon-add.svg';

const emit = defineEmits<{
  (e: 'onClick'): void;
}>();
</script>

<style scoped>
.git__add {
  border: 1px solid var(--color-border);
  width: 100%;
  border-radius: 4px;
}

.git__add:hover {
  border: 1px solid var(--color-secondary);
}
.git__add:hover .git__add--wrapper > button > svg {
  fill: var(--color-secondary);
}

.git__add--wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.git__add--wrapper > button {
  width: 100%;
  height: 100%;
  padding: 16px 0;
}
</style>
