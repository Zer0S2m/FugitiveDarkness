<script setup lang="ts">
import IconAllerError from '@/assets/icon-alert-error.svg';
import type { IError } from '@/types/api';

defineProps<{
  error: IError;
}>();
</script>

<template>
  <div class="alert-error">
    <div class="alert-error__wrapper">
      <div class="alert-error__icon">
        <IconAllerError />
      </div>
      <div class="alert-error__body">
        <h4 class="alert-error-body--type">{{ error.type }}</h4>
        <p class="alert-error-body--message">{{ error.message }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.alert-error {
  background-color: var(--color-alert-error);
  border-radius: 12px;
}

.alert-error__wrapper {
  padding: 12px;
  display: flex;
  align-items: center;
}

.alert-error__icon {
  margin-right: 20px;
}

.alert-error__icon > svg {
  height: 24px;
  width: 24px;
}

.alert-error-body--type {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
}
</style>
