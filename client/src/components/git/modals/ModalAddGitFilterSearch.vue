<template>
  <VueFinalModal
    modal-id="modalAddGitFilterSearch"
    class="modal"
    content-class="modal-content modal-content--add-git-repository"
  >
    <h3 class="modal-title">{{ title }}</h3>
    <div class="modal-wrapper-form">
      <Vueform
        v-model="dataForm"
        :sync="true"
        @submit="onAddGitFilterSearch"
      >
        <TextElement
          name="titleFilter"
          label="Title"
        />
      </Vueform>
      <div class="modal-button__block">
        <button
          class="modal-add-button"
          @click="onAddGitFilterSearch"
        >
          Add
        </button>
      </div>
    </div>
  </VueFinalModal>
</template>

<script setup lang="ts">
import { useVfm, VueFinalModal } from 'vue-final-modal';
import { useGitFilterSearchState } from '@/stores/useGitFilterSearchState';
import type { ICreateGitFilterSearch } from '@/types/gitFilterSearch';

const useGitFilterSearchStore = useGitFilterSearchState();
const useVfmStore = useVfm();

defineProps<{
  title: string;
}>();

const dataForm: {
  titleFilter: string;
} = {
  titleFilter: ''
};

const onAddGitFilterSearch = async (): Promise<void> => {
  await useVfmStore.close('modalAddGitFilterSearch');

  await useGitFilterSearchStore.createGitFilterSearch(<ICreateGitFilterSearch>{
    title: dataForm.titleFilter,
    filter: useGitFilterSearchStore.activeFilterForCreate
  });

  dataForm.titleFilter = '';
};
</script>

<style scoped></style>
