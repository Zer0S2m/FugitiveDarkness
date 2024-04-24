<script setup lang="ts">
import { RouterView } from 'vue-router';
import Header from '@/components/common/Header.vue';
import { ModalsContainer } from 'vue-final-modal';
import ModalAddGitMatcherNote from '@/components/git/modals/ModalAddGitMatcherNote.vue';
import ModalAddGitFilterSearch from '@/components/git/modals/ModalAddGitFilterSearch.vue';
import ModalSettingsGitRepository from '@/components/git/modals/ModalSettingsGitRepository.vue';
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
  <ModalsContainer />
</template>

<style scoped>
.app-wrapper {
  height: calc(100vh - var(--height-header));
  padding: 12px;
  overflow: hidden;
}
</style>
