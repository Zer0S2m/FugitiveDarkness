<template>
  <h4>{{ titleFile }}</h4>
  <div
    v-if="notesInternal.size"
    v-for="(noteInternal, noteInternalIndex) in notesInternal.entries()"
    :key="noteInternal[0] + '-' + noteInternalIndex + '-' + titleFile"
  >
    <Code
      class="block--code"
      :line-numbers="[noteInternal[1][0].line_number]"
      :code="noteInternal[0]"
      :language="language"
    />
    <ul
      class="matcher-note-item-file--items"
      v-if="notesInternal.size"
    >
      <li
        class="matcher-note-item-file--item"
        v-for="noteValue in noteInternal[1]"
        :key="noteValue.id"
      >
        <div class="matcher-note-item-file-item--wrapper">
          <p>{{ noteValue.value }}</p>
          <div class="matcher-note-item-file-item--tools">
            <button
              @click="onEditMatcherNote(noteValue.id)"
              class="edit"
            >
              <IconNote />
            </button>
            <button
              @click="onDeleteMatcherNote(noteValue.id)"
              class="delete"
            >
              <IconDelete />
            </button>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import IconNote from '@/assets/icon-note.svg';
import IconDelete from '@/assets/icon-delete.svg';
import { type IMatchNote } from '@/types/matcherNote';
import Code from '@/components/common/Code.vue';
import { type ComputedRef, computed } from 'vue';
import { useMatcherNoteState } from '@/stores/useMatcherNoteState';
import { useVfm } from 'vue-final-modal';

const useMatcherNoteStore = useMatcherNoteState();
const useVfmStore = useVfm();

// code line -> matcher note
const notesInternal: ComputedRef<Map<string, IMatchNote[]>> = computed(() => {
  const readyNotesInternal: Map<string, IMatchNote[]> = new Map<string, IMatchNote[]>();
  props.notes.forEach((note) => {
    if (readyNotesInternal.has(note.line)) {
      // @ts-ignore
      readyNotesInternal.get(note.line).push(note);
    } else {
      readyNotesInternal.set(note.line, [note]);
    }
  });
  return readyNotesInternal;
});

const props = defineProps<{
  titleFile: string;
  notes: IMatchNote[]; // Notes already sorted into files
}>();

const language = computed((): string => {
  const splitTitleFile: string[] = props.titleFile.split('/');
  const splitTitleFileName: string[] = splitTitleFile[splitTitleFile.length - 1].split('.');

  return splitTitleFileName[splitTitleFileName.length - 1];
});

const onDeleteMatcherNote = async (id: number): Promise<void> => {
  await useMatcherNoteStore.deleteMatcherNote(id);
};

const onEditMatcherNote = (id: number): void => {
  const currentNote: IMatchNote | undefined = props.notes.find((note) => note.id === id);

  if (currentNote !== undefined) {
    useMatcherNoteStore.setActiveDataIsCreate(false);
    useMatcherNoteStore.setActiveDataIsEdit(true);
    useMatcherNoteStore.setActiveDataCurrentId(id);
    useMatcherNoteStore.setActiveDataForCreateOrEdit({
      value: currentNote.value,
      file: currentNote.file,
      line: currentNote.line,
      lineNumber: currentNote.line_number,
      gitRepositoryId: currentNote.git_repository_id
    });

    useVfmStore.open('modalAddMatcherNote');
  }
};
</script>

<style scoped>
.block--code {
  margin: 12px 0;
}

.matcher-note-item-file--items {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-column-gap: 8px;
  grid-row-gap: 2px;
  margin-left: 12px;
}

.matcher-note-item-file--item {
  display: flex;
  width: 100%;
  margin-bottom: 8px;

  border-radius: 4px;
  border: 1px solid var(--color-border);
}

.matcher-note-item-file-item--wrapper {
  width: 100%;
  padding: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.matcher-note-item-file-item--tools {
  display: flex;
}

.matcher-note-item-file-item--tools > .edit {
  margin-right: 8px;
}

.matcher-note-item-file-item--tools > .delete > svg,
.matcher-note-item-file-item--tools > .edit > svg {
  width: 20px;
  height: 20px;
}
</style>
