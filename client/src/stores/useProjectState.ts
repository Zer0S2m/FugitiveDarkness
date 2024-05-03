import { defineStore } from 'pinia';
import type { Ref } from 'vue';
import { ref } from 'vue';
import type {
  IProjectFileComment,
  IProjectFileCommit,
  IProjectFileInfoCountFile,
  IProjectItemFile,
  IResponseProjectFileAllCommits,
  IResponseProjectFileComment,
  IResponseProjectFileCommit,
  IResponseProjectFileInfoCountFile,
  IResponseProjectFileTodos,
  IResponseProjectItemFile
} from '@/types/project';
import api from '@/services/api';
import type { AxiosResponse } from 'axios';
import type { ITree } from '@/types/tree';
import type { ISearchFoundByGrepGitRepository } from '@/types/gitRepository';
import { TypeFileLineCode } from '@/enums/project';
import { instanceIResponseError } from '@/utils/defenders';
import type { IError } from '@/types/api';

export const useProjectState = defineStore('project', () => {
  const activeProjectId: Ref<number> = ref(0);
  const activeProjectFilesTree: Ref<IProjectItemFile | undefined> = ref(undefined);

  // FILES
  const isProjectFilesTreeLoading: Ref<boolean> = ref(false);
  const activeProjectFile: Ref<ITree | null> = ref(null);

  // COMMENTS
  const isProjectFileCommentsLoading: Ref<boolean> = ref(false);
  const projectFileComments: Ref<IProjectFileComment[]> = ref([]);

  // COMMITS
  const projectFileFirstCommit: Ref<{
    data: IProjectFileCommit | null;
    error: IError | null;
  }> = ref({
    data: null,
    error: null
  });
  const projectFileLastCommit: Ref<{
    data: IProjectFileCommit | null;
    error: IError | null;
  }> = ref({
    data: null,
    error: null
  });
  const projectFileAllCommit: Ref<IProjectFileCommit[] | null> = ref(null);
  const isProjectFileFirstCommitLoading: Ref<boolean> = ref(false);
  const isProjectFileLastCommitLoading: Ref<boolean> = ref(false);
  const isProjectFileAllCommitLoading: Ref<boolean> = ref(false);

  // TODOS
  const projectFileTodos: Ref<ISearchFoundByGrepGitRepository[]> = ref([]);
  const isProjectFileTodosLoading: Ref<boolean> = ref(false);

  // COUNT LINES
  const projectFileCountLines: Ref<IProjectFileInfoCountFile[]> = ref([]);
  const isProjectFileCountLinesLoading: Ref<boolean> = ref(false);

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

  const setActiveProjectFile = (projectFile: ITree): void => {
    activeProjectFile.value = projectFile;
  };

  const loadProjectFileFirstCommit = async (): Promise<void> => {
    if (!activeProjectFilesTree) {
      return;
    }

    isProjectFileFirstCommitLoading.value = true;

    const response: AxiosResponse<IResponseProjectFileCommit> = await api.getProjectFileFirstCommit(
      activeProjectId.value,
      // @ts-ignore
      activeProjectFile.value.path
    );

    if (response.status === 404) {
      if (instanceIResponseError(response.data)) {
        projectFileFirstCommit.value = {
          data: null,
          // @ts-ignore
          error: response.data as IError
        };
      }

      isProjectFileFirstCommitLoading.value = false;

      return;
    }

    projectFileFirstCommit.value = {
      data: response.data.info,
      error: null
    };

    isProjectFileFirstCommitLoading.value = false;
  };

  const loadProjectFileLastCommit = async (): Promise<void> => {
    if (!activeProjectFilesTree) {
      return;
    }

    isProjectFileLastCommitLoading.value = true;

    const response: AxiosResponse<IResponseProjectFileCommit> = await api.getProjectFileLastCommit(
      activeProjectId.value,
      // @ts-ignore
      activeProjectFile.value.path
    );

    if (response.status === 404) {
      if (instanceIResponseError(response.data)) {
        projectFileLastCommit.value = {
          data: null,
          // @ts-ignore
          error: response.data as IError
        };
      }

      isProjectFileLastCommitLoading.value = false;

      return;
    }

    projectFileLastCommit.value = {
      data: response.data.info,
      error: null
    };

    isProjectFileLastCommitLoading.value = false;
  };

  const loadProjectFileAllCommit = async (): Promise<void> => {
    if (!activeProjectFilesTree) {
      return;
    }

    isProjectFileAllCommitLoading.value = true;

    const response: AxiosResponse<IResponseProjectFileAllCommits> =
      await api.getProjectFileAllCommit(
        activeProjectId.value,
        // @ts-ignore
        activeProjectFile.value.path
      );

    projectFileAllCommit.value = response.data.info;

    isProjectFileAllCommitLoading.value = false;
  };

  const loadProjectFileTodos = async (): Promise<void> => {
    if (!activeProjectFilesTree) {
      return;
    }

    isProjectFileTodosLoading.value = true;

    const response: AxiosResponse<IResponseProjectFileTodos> = await api.getProjectFileTodos(
      activeProjectId.value,
      {
        // @ts-ignore
        isFile: activeProjectFile.value.isFile,
        // @ts-ignore
        isDirectory: activeProjectFile.value.isDirectory,
        // @ts-ignore
        file: activeProjectFile.value?.path
      }
    );

    projectFileTodos.value = response.data.todos;

    isProjectFileTodosLoading.value = false;
  };

  const loadProjectFileComments = async (): Promise<void> => {
    isProjectFileCommentsLoading.value = true;

    const response: AxiosResponse<IResponseProjectFileComment> = await api.getProjectFileComments();

    projectFileComments.value = response.data.comments;

    isProjectFileCommentsLoading.value = false;
  };

  const loadProjectFileCountLines = async (): Promise<void> => {
    if (!activeProjectFilesTree) {
      return;
    }

    isProjectFileCountLinesLoading.value = true;

    const response: AxiosResponse<IResponseProjectFileInfoCountFile> =
      await api.getProjectFileInfoCountLine(
        activeProjectId.value,
        // @ts-ignore
        activeProjectFile.value?.path,
        activeProjectFile.value?.isDirectory ? TypeFileLineCode.DIRECTORY : TypeFileLineCode.FILE
      );

    projectFileCountLines.value = response.data.codeLines;

    isProjectFileCountLinesLoading.value = false;
  };

  const resetStatistics = (): void => {
    isProjectFileFirstCommitLoading.value = false;
    isProjectFileLastCommitLoading.value = false;
    isProjectFileAllCommitLoading.value = false;
    isProjectFileTodosLoading.value = false;
    isProjectFileCountLinesLoading.value = false;
    projectFileFirstCommit.value = {
      data: null,
      error: null
    };
    projectFileLastCommit.value = {
      data: null,
      error: null
    };
    projectFileAllCommit.value = null;
    projectFileTodos.value = [];
    projectFileCountLines.value = [];
  };

  return {
    activeProjectId,
    activeProjectFilesTree,
    activeProjectFile,
    isProjectFilesTreeLoading,
    isProjectFileFirstCommitLoading,
    isProjectFileLastCommitLoading,
    isProjectFileAllCommitLoading,
    projectFileFirstCommit,
    projectFileLastCommit,
    projectFileAllCommit,
    projectFileTodos,
    isProjectFileTodosLoading,
    isProjectFileCommentsLoading,
    projectFileComments,
    isProjectFileCountLinesLoading,
    projectFileCountLines,

    setActiveProject,
    loadProjectFilesTree,
    setActiveProjectFile,
    loadProjectFileFirstCommit,
    loadProjectFileLastCommit,
    loadProjectFileAllCommit,
    loadProjectFileTodos,
    loadProjectFileComments,
    loadProjectFileCountLines,
    resetStatistics
  };
});
