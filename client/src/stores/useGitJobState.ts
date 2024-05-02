import { defineStore } from 'pinia';
import { type Ref, ref } from 'vue';
import type {
  IGitJob,
  IGitJobCreate,
  IGitJobEdit,
  IResponseGitJob,
  IResponseGitJobCreate
} from '@/types/gitJob';
import api from '@/services/api';
import type { AxiosResponse } from 'axios';

export const useGitJobState = defineStore('git-jobs', () => {
  const isGitJobsLoading: Ref<boolean> = ref(false);
  const isGitJobLoad: Ref<boolean> = ref(false);
  const gitJobs: Ref<IGitJob[]> = ref([]);
  const activeGitJobId: Ref<number> = ref(-1);

  const getGitJobById = (id: number): IGitJob | undefined => {
    return gitJobs.value.find((gitJob: IGitJob): boolean => {
      return gitJob.id === id;
    });
  };

  const setActiveGitJobId = (id: number): void => {
    activeGitJobId.value = id;
  };

  const loadGitJobs = async (): Promise<void> => {
    if (isGitJobLoad.value) {
      return;
    }

    isGitJobsLoading.value = true;

    const response: AxiosResponse<IResponseGitJob> = await api.getAllGitJobs();
    gitJobs.value = response.data.gitJobs;

    isGitJobsLoading.value = false;
    isGitJobLoad.value = true;
  };

  const addGitJob = async (data: IGitJobCreate): Promise<void> => {
    const payload: IGitJobCreate = {
      cron: String(data.cron),
      type: data.type,
      gitRepositoryId: Number(data.gitRepositoryId)
    };
    const response: AxiosResponse<IResponseGitJobCreate> = await api.createGitJob(payload);

    if (response.status === 201) {
      gitJobs.value.push(response.data.gitJob);
    }
  };

  const deleteGitJobById = async (id: number): Promise<void> => {
    gitJobs.value = gitJobs.value.filter((gitJob: IGitJob): boolean => {
      return gitJob.id !== id;
    });

    await api.deleteGitJob(id);
  };

  const editGitJobById = async (id: number, data: IGitJobEdit): Promise<void> => {
    await api.editGitJob(id, data);

    gitJobs.value.forEach((gitJob: IGitJob): void => {
      if (gitJob.id === id) {
        gitJob.cron = data.cron;
      }
    });
  };

  return {
    isGitJobsLoading,
    isGitJobLoad,
    gitJobs,
    activeGitJobId,

    getGitJobById,
    setActiveGitJobId,
    loadGitJobs,
    addGitJob,
    deleteGitJobById,
    editGitJobById
  };
});
