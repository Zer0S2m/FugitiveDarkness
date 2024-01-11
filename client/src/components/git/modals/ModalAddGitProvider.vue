<template>
  <VueFinalModal
    class="modal"
    content-class="modal-content modal-content--add-git-provider"
  >
    <h3 class="modal-title">{{ title }}</h3>
    <div class="modal-wrapper-form">
      <div
        class="modal-errors"
        v-if="!useGitProviderStore.stateFormAddGitProviderErrors.success"
      >
        <div class="modal-errors--wrapper">
          <p class="modal-errors-text">
            {{ useGitProviderStore.stateFormAddGitProviderErrors.msg }}
          </p>
        </div>
      </div>
      <Vueform v-model="dataForm">
        <TextElement
          name="target"
          label="Organization or user name"
        />
        <RadiogroupElement
          name="type"
          :items="{
            GITHUB: GitProviderType.GITHUB.toString(),
            GITLAB: GitProviderType.GITLAB.toString()
          }"
          label="Provider"
          default="GITHUB"
        />
        <CheckboxElement
          class="checkbox-org"
          name="isOrg"
          label="Is the provider an organization"
        >
          Is organization
        </CheckboxElement>
        <CheckboxElement
          label="Is the provider a user"
          name="isUser"
        >
          Is user
        </CheckboxElement>
      </Vueform>
      <div class="modal-button__block">
        <button
          class="modal-add-button"
          @click="emit('confirm', dataForm)"
        >
          Add
        </button>
      </div>
    </div>
  </VueFinalModal>
</template>

<script setup lang="ts">
import type { IInstallGitProvider } from '@/types/gitProvider';
import { GitProviderType } from '@/enums/gitProvider';
import { VueFinalModal } from 'vue-final-modal';
import { useGitProviderState } from '@/stores/useGitProviderState';

const useGitProviderStore = useGitProviderState();

defineProps<{
  title?: string;
}>();

const dataForm: IInstallGitProvider = {
  type: GitProviderType.GITHUB,
  isOrg: false,
  isUser: false,
  target: ''
};

const emit = defineEmits<{
  (e: 'confirm', dataForm: IInstallGitProvider): void;
}>();
</script>

<style scoped>
.checkbox-org {
  margin-right: 20px;
}
</style>
