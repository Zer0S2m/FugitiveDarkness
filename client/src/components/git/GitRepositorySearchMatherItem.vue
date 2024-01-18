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
    <h6 class="matcher-found--title">
      <a
        class="matcher-found--title_link"
        :href="matcher.link"
        target="_blank"
        >{{ lineSlice(matcher.filename, 80) }}</a
      >
      <button
        class="copy-path"
        @click="copyPath"
      >
        <IconCopy />
      </button>
      <a
        type="button"
        target="#"
        class="open-file"
        @click="onClickShowFile"
      >
        <IconView />
      </a>
    </h6>
    <div class="matcher-found__result">
      <div class="matcher-found__result--wrapper">
        <div class="matcher-found__result--lines">
          <p
            :class="!matchersLink.isSequel ? 'matcher-found__result--line-sequel' : ''"
            v-for="matchersLink in matchersLinks"
          >
            <a
              :href="matchersLink.link"
              target="_blank"
              >{{ matchersLink.lineNumber }}</a
            >
          </p>
        </div>
        <div class="matcher-found__result--codes">
          <div
            :class="collectCode.length === 1 ? 'code--one' : 'code--multi'"
            class="matcher-found__result--code"
            v-for="code in collectCode"
          >
            <Highlightjs
              class="code"
              :code="code"
              :pattern="useGitRepositoryStore.filtersForSearch.pattern"
              :language="matcher.extension"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import IconCopy from '@/assets/icon-copy.svg';
import IconView from '@/assets/icon-view.svg';
import type {
  IMatcherFoundByGrepGitRepository,
  ISearchFoundByGrepGitRepository
} from '@/types/gitRepository';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import Highlightjs from '@lib/highlightjs';
import { computed } from 'vue';
import { lineSlice } from '@/utils/stringFormat';
import router from '@/router';

const useGitRepositoryStore = useGitRepositoryState();

const props = defineProps<{
  matcher: ISearchFoundByGrepGitRepository;
  groupRepository: string;
  projectRepository: string;
}>();

const collectCode = computed((): string[] => {
  const codes: string[] = [];
  let code: string = '';

  props.matcher.matchers.forEach((matcher, index) => {
    if (matcher.previewLast !== null && matcher.previewLast.length !== 0) {
      matcher.previewLast
        .sort((matcherPreviewLastFirst, matcherPreviewLastSecond) => {
          return matcherPreviewLastFirst.lineNumber - matcherPreviewLastSecond.lineNumber;
        })
        .forEach((matcherPreviewLast) => {
          code += `${matcherPreviewLast.matcher}\n`;
        });
    }

    code += `${matcher.matcher}\n`;

    if (matcher.previewNext !== null && matcher.previewNext.length !== 0) {
      matcher.previewNext
        .sort((matcherPreviewNextFirst, matcherPreviewNextSecond) => {
          return matcherPreviewNextFirst.lineNumber - matcherPreviewNextSecond.lineNumber;
        })
        .forEach((matcherPreviewNext) => {
          code += `${matcherPreviewNext.matcher}\n`;
        });
    }

    if (!getIsSequelLineCodeMatcher(matcher, index)) {
      codes.push(code);
      code = '';
    }
  });

  if (code.length !== 0) {
    codes.push(code);
    code = '';
  }

  return codes.length ? codes : [code];
});

const matchersLinks = computed((): { link: string; lineNumber: number; isSequel: boolean }[] => {
  const links: { link: string; lineNumber: number; isSequel: boolean }[] = [];

  props.matcher.matchers.forEach((matcher, indexMatcher) => {
    if (matcher.previewLast !== null && matcher.previewLast.length !== 0) {
      matcher.previewLast
        .sort((matcherPreviewLastFirst, matcherPreviewLastSecond) => {
          return matcherPreviewLastFirst.lineNumber - matcherPreviewLastSecond.lineNumber;
        })
        .forEach((matcherPreviewLast) => {
          links.push({
            link: matcherPreviewLast.link,
            lineNumber: matcherPreviewLast.lineNumber,
            isSequel: true
          });
        });
    }

    links.push({
      link: matcher.link,
      lineNumber: matcher.lineNumber,
      isSequel: true
    });

    if (matcher.previewNext !== null && matcher.previewNext.length !== 0) {
      matcher.previewNext
        .sort((matcherPreviewNextFirst, matcherPreviewNextSecond) => {
          return matcherPreviewNextFirst.lineNumber - matcherPreviewNextSecond.lineNumber;
        })
        .forEach((matcherPreviewNext, indexPreviewNext) => {
          if (
            matcher.previewNext?.length === indexPreviewNext + 1 &&
            props.matcher.matchers.length !== indexMatcher - 1
          ) {
            links.push({
              link: matcherPreviewNext.link,
              lineNumber: matcherPreviewNext.lineNumber,
              isSequel: false
            });
          } else {
            links.push({
              link: matcherPreviewNext.link,
              lineNumber: matcherPreviewNext.lineNumber,
              isSequel: true
            });
          }
        });
    }
  });

  return links;
});

const getIsSequelLineCodeMatcher = (matcher: IMatcherFoundByGrepGitRepository, index: number) => {
  return !(
    props.matcher.matchers.length - 1 > index &&
    matcher.lineNumber !== props.matcher.matchers[index + 1].lineNumber - 1
  );
};

const copyPath = (): void => {
  navigator.clipboard.writeText(props.matcher.filename);
};

const onClickShowFile = async (): Promise<void> => {
  useGitRepositoryStore.setActiveShowFile(
    {
      group: props.groupRepository,
      project: props.projectRepository,
      file: props.matcher.filename
    },
    props.matcher.extension
  );

  await router.push({
    name: 'git-show-file-from-git'
  });
};
</script>

<style scoped>
.copy-path,
.open-file {
  cursor: pointer;
  display: inline-flex;
}

.copy-path > svg,
.open-file > svg {
  transform: scale(0.55);
}
.copy-path:hover svg,
.open-file:hover > svg {
  fill: var(--color-secondary);
}

.copy-path > svg:hover,
.open-file > svg:hover {
  fill: var(--color-secondary);
}
</style>
