<template>
  <ContainerCardList v-if="!error">
    <GitRepositoryInProviderItem
      :item="item"
      v-for="item in items"
    />
  </ContainerCardList>
  <div
    class="error"
    v-if="error"
  >
    <div class="error__wrapper">
      <h6 class="error__title">{{ error.type }}</h6>
      <p class="error__text">{{ error.message }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import ContainerCardList from '@/components/common/ContainerCardList.vue';
import type { IGitRepositoryInProvider } from '@/types/gitProvider';
import GitRepositoryInProviderItem from '@/components/git/GitRepositoryInProviderItem.vue';
import type { IError } from '@/types/api';

defineProps<{ items: IGitRepositoryInProvider[]; error: IError | null }>();
</script>

<style>
.error {
  margin-top: 12px;
  display: flex;
}

.error__wrapper {
  padding: 20px;
  background-color: var(--color-danger);
  border-radius: 12px;
  color: var(--color-white);
}

.error__title {
  margin-bottom: 8px;
  font-size: 18px;
  font-weight: 500;
}

.error__text {
  font-size: 16px;
}
</style>
