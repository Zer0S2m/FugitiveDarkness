import { defineStore } from 'pinia';
import type { IResponseInstallError } from '@/types/api';
import type {
  IControlGitRepository,
  IFilterSearchGitRepository,
  IGitRepository,
  IInstallGitRepository,
  IResponseInstallingGitRepository,
  ISearchByGrepGitRepository
} from '@/types/gitRepository';
import api from '@/services/api';
import type { Ref } from 'vue';
import { ref } from 'vue';
import type { AxiosResponse } from 'axios';

export const useGitRepositoryState = defineStore('gitRepository', () => {
  const gitRepositories: Ref<IGitRepository[]> = ref([]);
  const resultSearchByGrepGitRepositories: Ref<ISearchByGrepGitRepository[]> = ref([]);
  const isLoading: Ref<boolean> = ref(true);
  const isLoadData: Ref<boolean> = ref(false);
  const isLoadingSearch: Ref<boolean> = ref(false);
  const filtersForSearch: Ref<IFilterSearchGitRepository> = ref({
    filters: {
      git: [] as IControlGitRepository[],
      includeExtensionFiles: [],
      excludeExtensionFiles: []
    },
    pattern: '' as string
  });
  const filtersByExtensionFiles: Ref<Map<string, { extension: string; isActive: boolean }[]>> = ref(
    new Map()
  );
  const filtersByRepository: Ref<Map<String, boolean>> = ref(new Map());
  const stateFormAddGitRepositoryErrors: Ref<{
    success: boolean;
    msg: string | null;
    type: string | null;
  }> = ref({
    success: true,
    msg: null,
    type: null
  });

  const loadGitRepositories = async (): Promise<void> => {
    if (isLoadData.value) return;

    isLoading.value = true;
    gitRepositories.value = (await api.getAllGitRepositories()).data.gitRepositories;
    isLoadData.value = true;
    isLoading.value = false;
  };

  const deleteGitRepository = async (gitRepo: IControlGitRepository): Promise<void> => {
    await api.deleteGitRepository(gitRepo);

    gitRepositories.value = gitRepositories.value.filter((gitRepository) => {
      return !(gitRepository.group_ === gitRepo.group && gitRepository.project === gitRepo.project);
    });
  };

  const setPatternFilterSearch = (pattern: string): void => {
    filtersForSearch.value.pattern = pattern;
  };

  const setGitRepositoryFilterSearch = (gitRepository: IControlGitRepository): void => {
    filtersForSearch.value.filters.git.push(gitRepository);
  };

  const removeGitRepositoryFilterSearch = (gitRepository: IControlGitRepository): void => {
    filtersForSearch.value.filters.git = filtersForSearch.value.filters.git.filter(
      (gitRepository_) => {
        return !(
          gitRepository_.group === gitRepository.group &&
          gitRepository_.project === gitRepository.project
        );
      }
    );
  };

  const getIsActivityGitRepositoryInFilter = (gitRepository: IControlGitRepository): boolean => {
    return (
      filtersForSearch.value.filters.git.find((gitRepository_) => {
        return (
          gitRepository_.group === gitRepository.group &&
          gitRepository_.project === gitRepository.project
        );
      }) != null
    );
  };

  const searchByGrep = async (): Promise<void> => {
    isLoadingSearch.value = true;
    filtersByExtensionFiles.value = new Map();

    resultSearchByGrepGitRepositories.value = (
      await api.searchByGrep(filtersForSearch.value)
    ).data.searchResult;

    resultSearchByGrepGitRepositories.value.forEach((searchResult) => {
      const repository = `${searchResult.group}/${searchResult.project}`;
      setExtensionFilesForFilter(repository, searchResult.extensionFiles);
      setFilterByRepository(repository, true);
    });

    isLoadingSearch.value = false;
  };

  const setExtensionFilesForFilter = (repository: string, extensionFiles: string[]): void => {
    filtersByExtensionFiles.value.set(repository, []);
    extensionFiles.forEach((extensionFile) => {
      setExtensionFileForFilter(repository, extensionFile, true);
    });
  };

  const setExtensionFileForFilter = (
    repository: string,
    extensionFile: string,
    isActive: boolean,
    isUpdate: boolean = false
  ): void => {
    if (filtersByExtensionFiles.value.has(repository)) {
      if (!isUpdate) {
        filtersByExtensionFiles.value.get(repository)?.push({
          extension: extensionFile,
          isActive
        });
      } else {
        filtersByExtensionFiles.value.get(repository)?.map((filtersByExtensionFile) => {
          if (filtersByExtensionFile.extension === extensionFile) {
            filtersByExtensionFile.isActive = isActive;
          }
        });
      }
    } else {
      filtersByExtensionFiles.value.set(repository, [
        {
          extension: extensionFile,
          isActive
        }
      ]);
    }
  };

  const getIsActiveGitRepositoryMatcherFileExtension = (
    repository: string,
    extensionFile: string
  ): boolean => {
    if (!hasExtensionFilesForFilterByRepository(repository)) {
      return false;
    }

    for (const filtersByExtensionFile of filtersByExtensionFiles.value.get(repository) ?? []) {
      if (filtersByExtensionFile.extension === extensionFile) {
        return filtersByExtensionFile.isActive;
      }
    }

    return false;
  };

  const hasExtensionFilesForFilterByRepository = (repository: string): boolean => {
    return filtersByExtensionFiles.value.has(repository);
  };

  const getIsActiveFilterByRepository = (repository: string): boolean => {
    if (!hasFilterByRepository(repository)) {
      return false;
    }

    return <boolean>filtersByRepository.value.get(repository);
  };

  const hasFilterByRepository = (repository: string): boolean => {
    return filtersByRepository.value.has(repository);
  };

  const setFilterByRepository = (repository: string, isActive: boolean): void => {
    filtersByRepository.value.set(repository, isActive);
  };

  const resetResult = (): void => {
    resultSearchByGrepGitRepositories.value = [];
    filtersForSearch.value = {
      filters: {
        git: [] as IControlGitRepository[],
        includeExtensionFiles: [],
        excludeExtensionFiles: []
      },
      pattern: '' as string
    };
    filtersByExtensionFiles.value = new Map();
    filtersByRepository.value = new Map();
  };

  const installingGitRepository = async (payload: IInstallGitRepository): Promise<void> => {
    const resultInstalled: AxiosResponse<IResponseInstallingGitRepository | IResponseInstallError> =
      await api.installGitRepository(payload);

    if (resultInstalled.status === 400) {
      const resultInstalledData: IResponseInstallError =
        resultInstalled.data as IResponseInstallError;
      stateFormAddGitRepositoryErrors.value = {
        success: false,
        msg: resultInstalledData.message,
        type: resultInstalledData.type
      };
      return;
    }

    const resultInstalledData: IResponseInstallingGitRepository =
      resultInstalled.data as IResponseInstallingGitRepository;

    gitRepositories.value = [
      ...gitRepositories.value,
      {
        create_at: '',
        group_: resultInstalledData.gitRepository.group,
        host: resultInstalledData.gitRepository.host,
        id: 0,
        is_load: resultInstalledData.isLoadGitRepository,
        project: resultInstalledData.gitRepository.project
      }
    ];
  };

  const clearStateFormAddGitRepositoryErrors = (): void => {
    stateFormAddGitRepositoryErrors.value = {
      success: true,
      msg: null,
      type: null
    };
  };

  const addIncludeExtensionFileForFilter = (fileExtension: string): void => {
    filtersForSearch.value.filters.includeExtensionFiles.push(fileExtension);
  };

  const addExcludeExtensionFileForFilter = (fileExtension: string): void => {
    filtersForSearch.value.filters.excludeExtensionFiles.push(fileExtension);
  };

  const removeIncludeExtensionFileForFilter = (fileExtension: string): void => {
    filtersForSearch.value.filters.includeExtensionFiles =
      filtersForSearch.value.filters.includeExtensionFiles.filter((item) => {
        return !(item === fileExtension);
      });
  };

  const removeExcludeExtensionFileForFilter = (fileExtension: string): void => {
    filtersForSearch.value.filters.excludeExtensionFiles =
      filtersForSearch.value.filters.excludeExtensionFiles.filter((item) => {
        return !(item === fileExtension);
      });
  };

  return {
    loadGitRepositories,
    deleteGitRepository,
    setPatternFilterSearch,
    setGitRepositoryFilterSearch,
    removeGitRepositoryFilterSearch,
    getIsActivityGitRepositoryInFilter,
    searchByGrep,
    setExtensionFilesForFilter,
    setExtensionFileForFilter,
    hasExtensionFilesForFilterByRepository,
    getIsActiveGitRepositoryMatcherFileExtension,
    hasFilterByRepository,
    getIsActiveFilterByRepository,
    setFilterByRepository,
    resetResult,
    installingGitRepository,
    clearStateFormAddGitRepositoryErrors,
    addIncludeExtensionFileForFilter,
    addExcludeExtensionFileForFilter,
    removeIncludeExtensionFileForFilter,
    removeExcludeExtensionFileForFilter,

    gitRepositories,
    resultSearchByGrepGitRepositories,
    isLoading,
    isLoadData,
    isLoadingSearch,
    filtersForSearch,
    filtersByExtensionFiles,
    filtersByRepository,
    stateFormAddGitRepositoryErrors
  };
});
