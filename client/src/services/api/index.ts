import axios, {type AxiosInstance, type AxiosResponse} from "axios";
import type {IControlGitRepository, IResponseGitRepository} from "@/types/gitRepository";

const apiClient: AxiosInstance = axios.create({
  baseURL: `${import.meta.env.VITE_FD_HOST_API}/api/v1`,
  headers: {
    "Content-type": "application/json",
  },
});

export default {
  async getAllGitRepositories(): Promise<AxiosResponse<IResponseGitRepository>> {
    return apiClient.get<IResponseGitRepository>("/git/repo")
  },
  async deleteGitRepository(data: IControlGitRepository): Promise<any> {
    return apiClient.delete("/git/repo/delete", {
      data
    })
  }
}
