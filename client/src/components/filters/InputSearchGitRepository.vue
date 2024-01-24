<template>
  <div>
    <BaseInput
      :model-value="useGitRepositoryStore.filtersForSearch.pattern"
      @update:model-value="onSetValueFromInput"
      v-on:keyup.enter="handler"
    />
  </div>
</template>

<script setup lang="ts">
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import BaseInput from '@/components/common/BaseInput.vue';

type HandlerFunction = (payload: KeyboardEvent) => void;

defineProps<{
  handler: HandlerFunction;
}>();
const useGitRepositoryStore = useGitRepositoryState();

const onSetValueFromInput = (newValue: string): void => {
  useGitRepositoryStore.setPatternFilterSearch(newValue);
};
</script>
