import { defineStore } from 'pinia';
import type { Ref } from 'vue';
import { ref } from 'vue';
import type { IProjectItemFile, IResponseProjectItemFile } from '@/types/project';
import api from '@/services/api';
import type { AxiosResponse } from 'axios';

export const useProjectState = defineStore('project', () => {
  const activeProjectId: Ref<number> = ref(0);
  const activeProjectFilesTree: Ref<IProjectItemFile | undefined> = ref(undefined);
  const isProjectFilesTreeLoading: Ref<boolean> = ref(false);

  const setActiveProject = (id: number): void => {
    activeProjectId.value = id;
  };

  const loadProjectFilesTree = async (): Promise<void> => {
    isProjectFilesTreeLoading.value = true;
    const response: AxiosResponse<IResponseProjectItemFile> = await api.getProjectFiles(
      activeProjectId.value
    );

    isProjectFilesTreeLoading.value = false;

    activeProjectFilesTree.value = response.data.files;
  };

  return {
    activeProjectId,
    activeProjectFilesTree,

    setActiveProject,
    loadProjectFilesTree,
    isProjectFilesTreeLoading
  };
});
