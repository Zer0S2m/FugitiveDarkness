<template>
  <input
    class="base-input"
    placeholder="..."
    :value="modelValue"
    @input="onEventInput"
  />
</template>

<script setup lang="ts">
const emit = defineEmits<{
  (e: 'update:modelValue', newValue: string);
}>();

defineProps<{
  modelValue: string;
}>();

const onEventInput = (newValue: Event): void => {
  // @ts-ignore
  emit('update:modelValue', newValue.target.value);
};
</script>
