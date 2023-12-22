<template>
  <div class="tools-wrapper">
    <div class="tools-wrapper__base">
      <div class="tools-wrapper__field">
        <InputSearchGitRepository />
      </div>
      <div>
        Result
      </div>
    </div>
    <FilterSearchGitRepository class="tools-wrapper__search"/>
  </div>
</template>

<script setup lang="ts">
import FilterSearchGitRepository from "@/components/filters/FilterSearchGitRepository.vue";
import InputSearchGitRepository from "@/components/filters/InputSearchGitRepository.vue";
</script>

<style scoped>
.tools-wrapper {
  display: flex;
  justify-content: space-between;
  height: 100%;
}

.tools-wrapper__base {
  width: 100%;
  margin-right: 20px;
}

.tools-wrapper__field {
  margin-bottom: 20px;
}

.tools-wrapper__search {
  width: 420px;
}
</style>