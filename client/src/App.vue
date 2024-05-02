<script setup lang="ts">
import { RouterView } from 'vue-router';
import Header from '@/components/common/Header.vue';
import { ModalsContainer } from 'vue-final-modal';
import ModalAddGitMatcherNote from '@/components/git/modals/ModalAddGitMatcherNote.vue';
import ModalAddGitFilterSearch from '@/components/git/modals/ModalAddGitFilterSearch.vue';
import ModalSettingsGitRepository from '@/components/git/modals/ModalSettingsGitRepository.vue';
import ModalAddGitJob from '@/components/git/modals/ModalAddGitJob.vue';
import ModalEditGitJob from '@/components/git/modals/ModalEditGitJob.vue';
</script>

<template>
  <Header />

  <div class="app-wrapper">
    <Suspense>
      <RouterView />
    </Suspense>
  </div>

  <ModalAddGitMatcherNote />
  <ModalAddGitFilterSearch title="Saving a search filter" />
  <ModalSettingsGitRepository title="Git repository settings" />
  <ModalAddGitJob />
  <ModalEditGitJob />
  <ModalsContainer />
</template>

<style scoped>
.app-wrapper {
  height: calc(100vh - var(--height-header));
  padding: 12px;
  overflow: hidden;
}
</style>
