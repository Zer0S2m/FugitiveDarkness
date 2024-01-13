<template>
  <div class="input-tags">
    <input
      class="search"
      v-model="newTag"
      type="text"
      @keydown.enter="addTag(newTag)"
      @keydown.prevent.tab="addTag(newTag)"
      @keydown.delete="newTag.length || removeTag(tags.length - 1)"
    />
    <ul class="tags">
      <li
        v-for="(tag, index) in tags"
        :key="tag"
        class="tag"
      >
        <span>{{ tag }}</span>
        <button
          class="delete"
          @click="removeTag(index)"
        >
          <IconDeleteBlack />
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import IconDeleteBlack from '@/assets/icon-delete-black.svg';
import { type Ref, ref } from 'vue';

const newTag: Ref<string> = ref('');
const props = defineProps<{ tags: string[] }>();
const emit = defineEmits<{
  (e: 'addTag', tag: string): void;
  (e: 'removeTag', index: number): void;
}>();

const addTag = (tag: string) => {
  emit('addTag', tag);

  props.tags.push(tag);
  newTag.value = '';
};

const removeTag = (index: number) => {
  emit('removeTag', index);

  props.tags.splice(index, 1);
};
</script>

<style scoped>
.tags {
  list-style: none;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  margin: 0;
  padding: 0;
}

.tag {
  background: var(--color-secondary);
  padding: 4px;
  border-radius: 4px;
  color: var(--vt-c-white);

  display: flex;
  align-items: center;
  justify-content: space-between;
}

.tag > span {
  margin-right: 2px;
}

.search {
  box-sizing: border-box;
  width: 100%;
  padding: 4px 8px;
  margin-bottom: 4px;

  background-color: var(--color-background);
  border: 1px solid var(--color-border);
  border-radius: 4px;
}

.search:hover,
.search:focus {
  border-color: var(--color-secondary);
}
</style>
