import { defineStore } from 'pinia';
import { ref, type Ref } from 'vue';
import type { ICreateMatcherNote, IEditMatcherNote, IMatchNote } from '@/types/matcherNote';
import api from '@/services/api';
import { type AxiosResponse } from 'axios';
import { type IResponseCreateMatcherNote } from '@/types/matcherNote';

export const useGitMatcherNoteState = defineStore('matcherNote', () => {
  const isLoading: Ref<boolean> = ref(true);
  const isLoadData: Ref<boolean> = ref(false);
  const matcherNotes: Ref<IMatchNote[]> = ref([]);
  const activeDataForCreateOrEdit: Ref<{
    isCreate: boolean;
    isEdit: boolean;
    currentId: number;
    data: ICreateMatcherNote;
  }> = ref({
    isCreate: true,
    isEdit: false,
    currentId: -1,
    data: {
      value: '',
      file: '',
      line: '',
      lineNumber: -1,
      gitRepositoryId: -1
    }
  });

  const loadMatcherNotes = async (): Promise<void> => {
    if (isLoadData.value) return;

    isLoading.value = true;

    matcherNotes.value = (await api.getAllMatcherNotes()).data.matcherNotes;

    isLoading.value = false;
    isLoadData.value = true;
  };

  const setActiveDataForCreateOrEdit = (newValue: ICreateMatcherNote): void => {
    activeDataForCreateOrEdit.value.data = newValue;
  };

  const setActiveDataIsCreate = (newValue: boolean): void => {
    activeDataForCreateOrEdit.value.isCreate = newValue;
  };

  const setActiveDataIsEdit = (newValue: boolean): void => {
    activeDataForCreateOrEdit.value.isEdit = newValue;
  };

  const setActiveDataCurrentId = (newValue: number): void => {
    activeDataForCreateOrEdit.value.currentId = newValue;
  };

  const deleteMatcherNote = async (id: number): Promise<void> => {
    await api.deleteMatcherNote(id);
    matcherNotes.value = matcherNotes.value.filter(
      (matcherNote: IMatchNote): boolean => matcherNote.id !== id
    );
  };

  const createMatcherNote = async (payload: ICreateMatcherNote): Promise<void> => {
    const createdMatcherNote: AxiosResponse<IResponseCreateMatcherNote> =
      await api.createMatcherNote(payload);

    matcherNotes.value.push(createdMatcherNote.data.matcherNote);

    resetActiveDataForCreateOrEdit();
  };

  const editMatcherNote = async (id: number, payload: IEditMatcherNote): Promise<void> => {
    await api.editMatcherNote(id, payload);
    matcherNotes.value.map((note: IMatchNote): void => {
      if (note.id === id) {
        note.value = payload.value;
      }
    });
    resetActiveDataForCreateOrEdit();
  };

  const resetActiveDataForCreateOrEdit = (): void => {
    activeDataForCreateOrEdit.value = {
      isCreate: true,
      isEdit: true,
      currentId: -1,
      data: {
        value: '',
        file: '',
        line: '',
        lineNumber: -1,
        gitRepositoryId: -1
      }
    };
  };

  return {
    isLoading,
    isLoadData,
    matcherNotes,
    activeDataForCreateOrEdit,

    loadMatcherNotes,
    setActiveDataForCreateOrEdit,
    deleteMatcherNote,
    createMatcherNote,
    resetActiveDataForCreateOrEdit,
    setActiveDataIsCreate,
    setActiveDataCurrentId,
    setActiveDataIsEdit,
    editMatcherNote
  };
});
