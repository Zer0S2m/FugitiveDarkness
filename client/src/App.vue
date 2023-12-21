<script setup lang="ts">
import { RouterView } from 'vue-router'
import Header from "@/components/common/Header.vue";
</script>

<template>
  <Header />
  <div class="app-wrapper">
    <Suspense>
      <RouterView />
    </Suspense>
  </div>
</template>
