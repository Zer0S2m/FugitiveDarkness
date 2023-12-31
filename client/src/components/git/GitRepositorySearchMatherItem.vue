<template>
  <div
    class="matcher-found"
    v-if="
      useGitRepositoryStore.getIsActiveGitRepositoryMatcherFileExtension(
        `${groupRepository}/${projectRepository}`,
        props.matcher.extension
      )
    "
  >
    <h6>
      <a
        :href="matcher.link"
        target="_blank"
        >{{ matcher.filename }}</a
      >
    </h6>
    <div class="matcher-found__result">
      <div class="matcher-found__result--wrapper">
        <div class="matcher-found__result--lines">
          <p v-for="matchersLink in matchersLinks">
            <a
              :href="matchersLink.link"
              target="_blank"
              >{{ matchersLink.lineNumber }}</a
            >
          </p>
        </div>
        <div class="matcher-found__result--code">
          <Highlightjs
            class="code"
            :code="collectCode"
            :language="matcher.extension"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { type ISearchFoundByGrepGitRepository } from '@/types/gitRepository';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import Highlightjs from '@lib/highlightjs';
import { computed } from 'vue';

const useGitRepositoryStore = useGitRepositoryState();

const props = defineProps<{
  matcher: ISearchFoundByGrepGitRepository;
  groupRepository: string;
  projectRepository: string;
}>();

const collectCode = computed((): string => {
  let code = '';

  props.matcher.matchers.forEach((matcher) => {
    if (matcher.previewLast !== null && matcher.previewLast.length !== 0) {
      if (matcher.previewLast.length > 1) {
        matcher.previewLast
          .sort((matcherPreviewLastFirst, matcherPreviewLastSecond) => {
            return matcherPreviewLastFirst.lineNumber - matcherPreviewLastSecond.lineNumber;
          })
          .forEach((matcherPreviewLast) => {
            code += `${matcherPreviewLast.matcher}\n`;
          });
      } else {
        matcher.previewLast.forEach((matcherPreviewLast) => {
          code += `${matcherPreviewLast.matcher}\n`;
        });
      }
    }
    code += `${matcher.matcher}\n`;
  });

  return code;
});

const matchersLinks = computed((): { link: string; lineNumber: number }[] => {
  const links: { link: string; lineNumber: number }[] = [];

  props.matcher.matchers.forEach((matcher) => {
    if (matcher.previewLast !== null && matcher.previewLast.length !== 0) {
      if (matcher.previewLast.length > 1) {
        matcher.previewLast
          .sort((matcherPreviewLastFirst, matcherPreviewLastSecond) => {
            return matcherPreviewLastFirst.lineNumber - matcherPreviewLastSecond.lineNumber;
          })
          .forEach((matcherPreviewLast) => {
            links.push({
              link: matcherPreviewLast.link,
              lineNumber: matcherPreviewLast.lineNumber
            });
          });
      } else {
        matcher.previewLast.forEach((matcherPreviewLast) => {
          links.push({
            link: matcherPreviewLast.link,
            lineNumber: matcherPreviewLast.lineNumber
          });
        });
      }
    }
    links.push({
      link: matcher.link,
      lineNumber: matcher.lineNumber
    });
  });

  return links;
});
</script>

<style scoped>
h6 > a,
.matcher-found__result--lines > p > a {
  text-decoration: none;
  color: var(--color-text);
}

h6 > a:hover,
.matcher-found__result--lines > p > a:hover {
  color: var(--color-secondary);
}

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
  line-height: 1.3;
}

.matcher-found__result--code {
  padding: 8px 8px 8px 0;
}

.matcher-found__result--code > p {
  font-family: 'Fira Code', serif;
  font-size: 14px;
}
</style>
