<template>
  <VueFinalModal
    class="modal"
    content-class="modal-content"
    modal-id="modalSettingsGitFilterSearch"
  >
    <h3 class="modal-title">{{ title }}</h3>
    <div class="modal-wrapper-form">
      <h4 class="modal-title--second">Parameters</h4>
      <ul class="parameters">
        <li class="parameter__item">
          <span>Is the repository unpacked</span>
          <IconCheck v-if="isUnpackingGitRepository" />
          <IconCancel v-if="!isUnpackingGitRepository" />
          <div
            class="parameter__item--tools"
            v-if="!isUnpackingGitRepository"
          >
            <button
              class="parameter__item-tool--unpack"
              @click="onClickUnpackGitRepository"
            >
              Unpack
            </button>
          </div>
        </li>
      </ul>
    </div>
  </VueFinalModal>
</template>

<script setup lang="ts">
import IconCheck from '@/assets/icon-check.svg';
import IconCancel from '@/assets/icon-cancel.svg';
import { useVfm, VueFinalModal } from 'vue-final-modal';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import { computed } from 'vue';

const useGitRepositoryStore = useGitRepositoryState();
const useVfmStore = useVfm();

defineProps<{
  title: string;
}>();

const isUnpackingGitRepository = computed((): boolean => {
  if (useGitRepositoryStore.activeSelectGitRepositorySettings !== null) {
    return useGitRepositoryStore.activeSelectGitRepositorySettings.is_unpacking;
  } else {
    return false;
  }
});

const onClickUnpackGitRepository = async (): Promise<void> => {
  useVfmStore.close('modalSettingsGitFilterSearch');

  if (useGitRepositoryStore.activeSelectGitRepositorySettings !== null) {
    await useGitRepositoryStore.unpackingGitRepository(
      useGitRepositoryStore.activeSelectGitRepositorySettings.group_,
      useGitRepositoryStore.activeSelectGitRepositorySettings.project
    );
  }
};
</script>

<style scoped>
.parameters {
  margin-top: 16px;
}

.parameter__item {
  display: flex;
  align-items: center;
}

.parameter__item > span {
  margin-right: 12px;
}

.parameter__item-tool--unpack {
  border: 1px solid var(--color-secondary);
  padding: 4px 20px;
  border-radius: 4px;
  margin-left: 20px;
}
</style>
