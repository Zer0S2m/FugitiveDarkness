<script setup lang="ts">
import { useRoute } from 'vue-router';
import { useProjectState } from '@/stores/useProjectState';
import { onMounted } from 'vue';
import { HalfCircleSpinner } from 'epic-spinners';
import ProjectItemTreeFileBrowser from '@/components/project/widget/ProjectItemTreeFileBrowser.vue';

const route = useRoute();
const useProjectStore = useProjectState();

onMounted(async (): Promise<void> => {
  useProjectStore.setActiveProject(parseInt(<string>route.params.id));

  await useProjectStore.loadProjectFilesTree();
});
</script>

<template>
  <div class="dashboard h100">
    <div class="dashboard--wrapper h100">
      <div class="dashboard__project h100">
        <div class="dashboard__project--wrapper h100">
          <div
            class="dashboard__project--loader"
            v-if="useProjectStore.isProjectFilesTreeLoading"
          >
            <HalfCircleSpinner
              :animation-duration="1000"
              :size="32"
              color="var(--color-secondary)"
            />
          </div>
          <ProjectItemTreeFileBrowser
            v-if="!useProjectStore.isProjectFilesTreeLoading"
            :tree="useProjectStore.activeProjectFilesTree"
          />
        </div>
      </div>
      <div class="dashboard__statistics">Statistics</div>
    </div>
  </div>
</template>

<style scoped>
.dashboard--wrapper {
  display: flex;
  justify-content: space-between;
}

.dashboard__project {
  width: 25%;
  border: 1px solid var(--color-secondary);
  border-radius: 4px;
}

.dashboard__project--wrapper {
  padding: 12px;
  overflow: auto;
}

.dashboard__project--loader {
  width: 100%;
  display: flex;
  justify-content: center;
}

.dashboard__statistics {
  width: 74%;
}
</style>
