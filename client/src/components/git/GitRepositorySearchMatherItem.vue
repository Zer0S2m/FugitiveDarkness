<template>
  <div class="matcher-found">
    <h6>{{ matcher.filename }}</h6>
    <div class="matcher-found__result">
      <div class="matcher-found__result--wrapper">
        <div class="matcher-found__result--lines">
          <p v-for="data in matcher.matchers">
            <span>{{ data.lineNumber }}</span>
          </p>
        </div>
        <div class="matcher-found__result--code">
          <p v-for="data in matcher.matchers">
            <span>{{ data.matcher }}</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { type ISearchFoundByGrepGitRepository } from '@/types/gitRepository';

defineProps<{
  matcher: ISearchFoundByGrepGitRepository;
}>();
</script>

<style scoped>
.matcher-found__result {
  border: 1px solid var(--color-border);
  width: 100%;
  border-radius: 4px;
  margin-top: 6px;
}

.matcher-found__result--wrapper {
  display: flex;
}

.matcher-found:hover .matcher-found__result {
  border: 1px solid var(--color-secondary);
}

.matcher-found__result--lines {
  margin-right: 30px;
  padding: 8px 12px 8px 0;
  max-width: 68px;
  width: 100%;
  background-color: var(--color-border);
}

.matcher-found__result--lines > p {
  width: 100%;
  text-align: end;
  font-family: 'Fira Code', serif;
  font-size: 14px;
}

.matcher-found__result--code {
  padding: 8px 8px 8px 0;
}

.matcher-found__result--code > p {
  font-family: 'Fira Code', serif;
  font-size: 14px;
}
</style>
