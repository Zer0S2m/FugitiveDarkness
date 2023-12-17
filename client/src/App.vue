<script setup lang="ts">
import { RouterView } from 'vue-router'
import Header from "@/components/common/Header.vue";
</script>

<template>
  <Header />
  <div class="app-wrapper">
    <RouterView />
  </div>
</template>
