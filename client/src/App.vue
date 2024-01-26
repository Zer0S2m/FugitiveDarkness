<script setup lang="ts">
import { RouterView } from 'vue-router';
import Header from '@/components/common/Header.vue';
import { ModalsContainer } from 'vue-final-modal';
import ModalAddMatcherNote from '@/components/matcherNote/modals/ModalAddMatcherNote.vue';
</script>

<template>
  <Header />
  <div class="app-wrapper">
    <Suspense>
      <RouterView />
    </Suspense>
  </div>
  <ModalAddMatcherNote />
  <ModalsContainer />
</template>

<style scoped>
.app-wrapper {
  height: 100%;
  padding: 20px 20px 0 20px;
}
</style>
