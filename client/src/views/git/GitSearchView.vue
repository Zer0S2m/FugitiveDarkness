<template>
  <div class="tools-wrapper">
    <div class="tools-wrapper__base scroll">
      <div class="tools-wrapper__field">
        <div class="tools-wrapper__block">
          <InputSearchGitRepository
            class="tools-wrapper__search"
            :handler="searchByGrep"
          />
          <ButtonSearchGitRepository @click="searchByGrep" />
        </div>
      </div>
      <div class="tools-wrapper__result">
        <GitRepositorySearchList
          :items="useGitRepositoryStore.resultSearchByGrepGitRepositories"
          v-if="!useGitRepositoryStore.isLoadingSearch"
        />
        <div
          class="tools-wrapper__search--loader"
          v-if="useGitRepositoryStore.isLoadingSearch"
        >
          <HalfCircleSpinner
            :animation-duration="1000"
            :size="40"
            color="var(--color-secondary)"
          />
        </div>
      </div>
    </div>
    <div>
      <SavedFiltersGit class="tools-wrapper__saved-filters" />
      <FilterSearchGitRepository class="tools-wrapper__filters" />
    </div>
  </div>
</template>

<script setup lang="ts">
import FilterSearchGitRepository from '@/components/filters/FilterSearchGitRepository.vue';
import InputSearchGitRepository from '@/components/filters/InputSearchGitRepository.vue';
import ButtonSearchGitRepository from '@/components/filters/ButtonSearchGitRepository.vue';
import { useGitRepositoryState } from '@/stores/useGitRepositoryState';
import GitRepositorySearchList from '@/components/git/container/GitRepositorySearchList.vue';
import { HalfCircleSpinner } from 'epic-spinners';
import router from '@/router';
import { useRoute } from 'vue-router';
import { onMounted } from 'vue';
import SavedFiltersGit from '@/components/filters/SavedFiltersGit.vue';

const useGitRepositoryStore = useGitRepositoryState();
const vueRouteStore = useRoute();

const searchByGrep = async () => {
  await useGitRepositoryStore.searchByGrep();
  await router.push({
    name: 'git-search',
    query: {
      q: useGitRepositoryStore.urlSearch['q'],
      repo: useGitRepositoryStore.urlSearch['repo'],
      includeFileExt: useGitRepositoryStore.urlSearch['includeFileExt'],
      excludeFileExt: useGitRepositoryStore.urlSearch['excludeFileExt']
    }
  });
};

const loadPage = async () => {
  if (!useGitRepositoryStore.isLoadData) {
    if (vueRouteStore.query['q'] && vueRouteStore.query['repo']) {
      await useGitRepositoryStore.parseUrlSearchAndLoadData(vueRouteStore.query);
    }
    return;
  }

  const query: {
    [key: string]: string;
  } = {
    q: useGitRepositoryStore.urlSearch['q'],
    repo: useGitRepositoryStore.urlSearch['repo']
  };

  if (
    useGitRepositoryStore.urlSearch['includeFileExt'] &&
    useGitRepositoryStore.urlSearch['includeFileExt'].length
  ) {
    query['includeFileExt'] = useGitRepositoryStore.urlSearch['includeFileExt'];
  }
  if (
    useGitRepositoryStore.urlSearch['excludeFileExt'] &&
    useGitRepositoryStore.urlSearch['excludeFileExt'].length
  ) {
    query['excludeFileExt'] = useGitRepositoryStore.urlSearch['excludeFileExt'];
  }

  await router.push({
    name: 'git-search',
    query
  });
};

onMounted(async () => {
  await loadPage();
});
</script>

<style scoped>
.tools-wrapper {
  display: flex;
  justify-content: space-between;
  height: 100%;
}

.tools-wrapper__base {
  width: 100%;
  height: 92vh;
  margin-right: 20px;
  padding-right: 12px;
  overflow-y: scroll;
}

.tools-wrapper__field {
  margin-bottom: 20px;
}

.tools-wrapper__block {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.tools-wrapper__search {
  width: 100%;
  margin-right: 12px;
}

.tools-wrapper__search--loader {
  display: flex;
  justify-content: center;
}

.tools-wrapper__saved-filters {
  width: 360px;
  margin-bottom: 12px;
}

.tools-wrapper__filters {
  width: 360px;
}
</style>
