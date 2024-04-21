<template>
  <VueFinalModal
    modal-id="modalAddMatcherNote"
    class="modal"
    content-class="modal-content modal-content--add-git-repository"
  >
    <h3 class="modal-title">
      {{
        useGitMatcherNoteStore.activeDataForCreateOrEdit.isCreate
          ? 'Add a note to a match'
          : 'Edit a note to a match'
      }}
    </h3>
    <div class="modal-wrapper-form">
      <Vueform
        @submit="onClickForm"
        v-model="rawDataForm"
        :sync="true"
      >
        <TextElement
          name="value"
          label="Value"
        />
      </Vueform>
      <div class="modal-button__block">
        <button
          class="modal-add-button"
          @click="onClickForm"
        >
          <span v-if="isCreateMatcherNote">Add</span>
          <span v-if="isEditMatcherNote">Edit</span>
        </button>
      </div>
    </div>
  </VueFinalModal>
</template>

<script setup lang="ts">
import { useVfm, VueFinalModal } from 'vue-final-modal';
import type { ICreateMatcherNote } from '@/types/matcherNote';
import { useGitMatcherNoteState } from '@/stores/useGitMatcherNoteState';
import type { Ref } from 'vue';
import { ref, watch } from 'vue';

const useGitMatcherNoteStore = useGitMatcherNoteState();
const useVfmStore = useVfm();
const readyDataForm: Ref<ICreateMatcherNote> = ref({
  value: '',
  file: '',
  line: '',
  lineNumber: -1,
  gitRepositoryId: -1
});
const rawDataForm: Ref<{
  value: string;
}> = ref({
  value: ''
});
const isCreateMatcherNote: Ref<boolean> = ref(true);
const isEditMatcherNote: Ref<boolean> = ref(false);

watch(
  () => useGitMatcherNoteStore.activeDataForCreateOrEdit.data,
  (newValue: ICreateMatcherNote) => {
    rawDataForm.value = {
      value: newValue.value
    };
    readyDataForm.value = newValue;
  }
);
watch(
  () => useGitMatcherNoteStore.activeDataForCreateOrEdit.isCreate,
  (newValue: boolean) => {
    isCreateMatcherNote.value = newValue;
  }
);
watch(
  () => useGitMatcherNoteStore.activeDataForCreateOrEdit.isEdit,
  (newValue: boolean) => {
    isEditMatcherNote.value = newValue;
  }
);

const onClickForm = async (): Promise<void> => {
  await useVfmStore.close('modalAddMatcherNote');
  useGitMatcherNoteStore.setActiveDataForCreateOrEdit({
    value: rawDataForm.value.value,
    file: readyDataForm.value.file,
    line: readyDataForm.value.line,
    lineNumber: readyDataForm.value.lineNumber,
    gitRepositoryId: readyDataForm.value.gitRepositoryId
  });

  if (isCreateMatcherNote.value && !isEditMatcherNote.value) {
    await onClickAddNote();
  } else if (isEditMatcherNote.value && !isCreateMatcherNote.value) {
    await onClickEditNote();
  }

  useGitMatcherNoteStore.resetActiveDataForCreateOrEdit();
};

const onClickAddNote = async (): Promise<void> => {
  await useGitMatcherNoteStore.createMatcherNote(
    useGitMatcherNoteStore.activeDataForCreateOrEdit.data
  );
};

const onClickEditNote = async (): Promise<void> => {
  await useGitMatcherNoteStore.editMatcherNote(
    useGitMatcherNoteStore.activeDataForCreateOrEdit.currentId,
    {
      value: rawDataForm.value.value
    }
  );
};
</script>

<style scoped></style>
