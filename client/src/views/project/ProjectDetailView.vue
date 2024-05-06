<script setup lang="ts">
import { useRoute } from 'vue-router';
import { useProjectState } from '@/stores/useProjectState';
import { onMounted } from 'vue';
import { HalfCircleSpinner } from 'epic-spinners';
import ProjectItemTreeFileBrowser from '@/components/project/widget/ProjectItemTreeFileBrowser.vue';
import ProjectItemCommit from '@/components/project/widget/ProjectItemCommit.vue';
import ProjectPanelAllCommits from '@/components/project/widget/ProjectPanelAllCommits.vue';
import ProjectItemTodoFile from '@/components/project/widget/ProjectItemTodoFile.vue';
import ProjectItemCountLines from '@/components/project/widget/ProjectItemCountLines.vue';

const route = useRoute();
const useProjectStore = useProjectState();

onMounted(async (): Promise<void> => {
  useProjectStore.setActiveProject(parseInt(<string>route.params.id));

  await Promise.all([
    new Promise(() => {
      useProjectStore.loadProjectFilesTree();
    }),
    new Promise(() => {
      useProjectStore.loadProjectFileComments();
    })
  ]);
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
      <div class="dashboard__statistics">
        <div class="dashboard__statistics--code-lines">
          <ProjectItemCountLines
            :is-loading="useProjectStore.isProjectFileCountLinesLoading"
            :code-lines="useProjectStore.projectFileCountLines"
          />
        </div>
        <div class="dashboard_statistics--commits">
          <ProjectItemCommit
            class="dashboard_statistics--commit"
            :is-loading="useProjectStore.isProjectFileLastCommitLoading"
            :commit="useProjectStore.projectFileLastCommit.data"
            :error="useProjectStore.projectFileLastCommit.error"
            title="Last commit"
          />
          <ProjectItemCommit
            class="dashboard_statistics--commit"
            :is-loading="useProjectStore.isProjectFileFirstCommitLoading"
            :commit="useProjectStore.projectFileFirstCommit.data"
            :error="useProjectStore.projectFileFirstCommit.error"
            title="First commit"
          />
        </div>
        <div class="dashboard__statistics--panel-commits">
          <ProjectPanelAllCommits
            :is-loading="useProjectStore.isProjectFileAllCommitLoading"
            :commits="useProjectStore.projectFileAllCommit"
          />
        </div>
        <div class="dashboard__statistics--todo">
          <ProjectItemTodoFile
            :is-loading="useProjectStore.isProjectFileTodosLoading"
            :todo="useProjectStore.projectFileTodos"
          />
        </div>
      </div>
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
  overflow-y: auto;
}

.dashboard_statistics--commits {
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
}

.dashboard_statistics--commit {
  width: 49%;
  max-width: 100%;
}

.dashboard__statistics--panel-commits {
  margin-top: 12px;
}

.dashboard__statistics--todo {
  margin-top: 12px;
}
</style>
