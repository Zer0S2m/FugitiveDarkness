<script setup lang="ts">
import { RouterView } from 'vue-router';
import Header from '@/components/common/Header.vue';
import { ModalsContainer } from 'vue-final-modal';
</script>

<template>
  <Header />
  <div class="app-wrapper">
    <Suspense>
      <RouterView />
    </Suspense>
  </div>
  <ModalsContainer />
</template>

<style scoped>
.app-wrapper {
  height: 100%;
  padding: 20px;
}
</style>
