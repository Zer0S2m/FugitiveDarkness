<template>
  <div
    class="matcher-note--block"
    v-for="matcherNote in matcherNotes"
    :key="matcherNote.gitRepoTitle"
  >
    <h4 class="title-repo">{{ matcherNote.gitRepoTitle }}</h4>
    <ul class="matcher-note--items">
      <li
        class="matcher-note--item"
        v-for="matcherNoteItemFile in matcherNote.notesByFile.entries()"
        :key="matcherNoteItemFile[0] + '-' + matcherNote.gitRepoTitle"
      >
        <MatcherNoteItem
          :title-file="matcherNoteItemFile[0]"
          :notes="matcherNoteItemFile[1]"
        />
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import type { IMatchNote } from '@/types/matcherNote';
import { computed, type ComputedRef } from 'vue';
import type { IGitRepository } from '@/types/gitRepository';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import MatcherNoteItem from '@/components/matcherNote/MatcherNoteItem.vue';

const useGitRepositoryStore = useGitRepositoryState();

interface IMatcherNoteReady {
  gitRepoTitle: string;
  notesByFile: Map<string, IMatchNote[]>; // file -> notes
}

const props = defineProps<{
  items: IMatchNote[];
}>();

const matcherNotes: ComputedRef<IMatcherNoteReady[]> = computed(() => {
  const readyData: Map<string, IMatcherNoteReady[]> = new Map<string, IMatcherNoteReady[]>();
  const readyNotes: IMatcherNoteReady[] = [];

  props.items.forEach((item) => {
    const repo: IGitRepository | undefined = useGitRepositoryStore.getRepositoryById(
      item.git_repository_id
    );

    if (repo) {
      const repoTitle: string = `${repo.group_}/${repo.project}`;

      if (readyData.has(repoTitle)) {
        const matherNotesByGitRepoTitle: IMatcherNoteReady[] | undefined = readyData.get(repoTitle);

        if (matherNotesByGitRepoTitle) {
          if (matherNotesByGitRepoTitle[0].notesByFile.has(item.file)) {
            const notesByFile: IMatchNote[] | undefined =
              matherNotesByGitRepoTitle[0].notesByFile.get(item.file);
            if (notesByFile) {
              notesByFile.push(item);
              matherNotesByGitRepoTitle[0].notesByFile.set(item.file, notesByFile);
            }
          } else {
            matherNotesByGitRepoTitle[0].notesByFile.set(item.file, [item]);
          }
        }
      } else {
        readyData.set(repoTitle, [
          {
            gitRepoTitle: repoTitle,
            notesByFile: new Map<string, IMatchNote[]>().set(item.file, [item])
          }
        ]);
      }
    }
  });

  readyData.forEach((value: IMatcherNoteReady[], _: string) => {
    readyNotes.push(...value);
  });

  return readyNotes;
});
</script>

<style scoped>
.matcher-note--block {
  margin-bottom: 20px;
}
.matcher-note--block:last-child {
  margin-bottom: 0;
}

.title-repo {
  margin-bottom: 12px;
  font-weight: 600;
}

.matcher-note--items {
  margin-left: 16px;
}

.matcher-note--item {
  margin-bottom: 20px;
}
.matcher-note--item:last-child {
  margin-bottom: 0;
}
</style>
