<template>
  <div class="matcher-found__result">
    <div class="matcher-found__result--wrapper">
      <div class="matcher-found__result--lines">
        <p
          v-for="(lineNumber, lineNumberIndex) in lineNumbers"
          :key="lineNumberIndex"
        >
          {{ lineNumber }}
        </p>
      </div>
      <div class="matcher-found__result--codes">
        <div class="code--one matcher-found__result--code">
          <Highlightjs
            class="code"
            :code="code"
            :language="language"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Highlightjs from '@lib/highlightjs';

defineProps<{
  lineNumbers: number[];
  code: string;
  language: string;
}>();
</script>

<style scoped></style>
