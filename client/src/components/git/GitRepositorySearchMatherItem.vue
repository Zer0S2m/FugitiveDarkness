<template>
  <div class="matcher-found">
    <h6>{{ matcher.filename }}</h6>
    <div class="matcher-found__result">
      <div class="matcher-found__result--wrapper">
        <p v-for="data in matcher.matchers">
          <span>{{data.lineNumber}}</span> - <span>{{data.matcher}}</span>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {type ISearchFoundByGrepGitRepository} from "@/types/gitRepository";

defineProps<{
  matcher: ISearchFoundByGrepGitRepository
}>()
</script>

<style scoped>
.matcher-found__result {
  border: 1px solid var(--color-border);
  width: 100%;
  border-radius: 4px;
  margin-top: 6px;
}

.matcher-found__result--wrapper {
  padding: 8px;
}

.matcher-found:hover .matcher-found__result {
  border: 1px solid var(--color-secondary);
}
</style>