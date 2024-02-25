import { defineStore } from 'pinia';
import type { IResponseInstallError } from '@/types/api';
import type {
  IControlGitRepository,
  IFilterSearchGitRepository,
  IGetFileFromGitRepository,
  IGitRepository,
  IInstallGitRepository,
  IResponseInstallingGitRepository,
  ISearchByGrepGitRepository
} from '@/types/gitRepository';
import api from '@/services/api';
import type { Ref } from 'vue';
import { ref } from 'vue';
import type { AxiosResponse } from 'axios';
import type { LocationQuery } from 'vue-router';

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
      excludeExtensionFiles: [],
      patternForIncludeFile: '',
      patternForExcludeFile: '',
      maxCount: -1,
      maxDepth: -1,
      context: -1,
      contextBefore: -1,
      contextAfter: -1
    },
    pattern: ''
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
  const urlSearch: Ref<{
    [key: string]: string;
  }> = ref({});
  const activeShowFile: Ref<{
    file: IGetFileFromGitRepository;
    language: string;
  }> = ref({
    file: {
      group: '',
      project: '',
      file: ''
    },
    language: ''
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
    setQueryParamsForSearchPage();

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
    resetAllFilters();

    resultSearchByGrepGitRepositories.value = [];
    filtersByExtensionFiles.value = new Map();
    filtersByRepository.value = new Map();
    urlSearch.value = {};
  };

  const resetAllFilters = (): void => {
    filtersForSearch.value = {
      filters: {
        git: [] as IControlGitRepository[],
        includeExtensionFiles: [],
        excludeExtensionFiles: [],
        patternForIncludeFile: '',
        patternForExcludeFile: '',
        maxCount: -1,
        maxDepth: -1,
        context: -1,
        contextBefore: -1,
        contextAfter: -1
      },
      pattern: ''
    };
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
        is_local: resultInstalledData.isLocalGitRepository,
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

  const updateGitRepositoryOperationFetch = async (
    gitRepo: IControlGitRepository
  ): Promise<void> => {
    gitRepositories.value.map(async (gitRepository) => {
      if (gitRepository.group_ === gitRepo.group && gitRepository.project === gitRepo.project) {
        gitRepository.is_load = false;
        await api.updateGitRepository(gitRepo);
      }
    });
  };

  const setQueryParamsForSearchPage = (): void => {
    let queryParamGitRepo = '';
    let queryParamIncludeFileExtension =
      filtersForSearch.value.filters.includeExtensionFiles.join(',');
    let queryParamExcludeFileExtension =
      filtersForSearch.value.filters.excludeExtensionFiles.join(',');

    filtersForSearch.value.filters.git.forEach((gitRepo: IControlGitRepository): void => {
      queryParamGitRepo += `${gitRepo.group}_${gitRepo.project}+`;
    });

    queryParamGitRepo = queryParamGitRepo.slice(0, -1);

    urlSearch.value['q'] = filtersForSearch.value.pattern;
    urlSearch.value['repo'] = queryParamGitRepo;

    if (queryParamIncludeFileExtension.length) {
      urlSearch.value['includeFileExt'] = queryParamIncludeFileExtension;
    } else {
      delete urlSearch.value['includeFileExt'];
    }
    if (queryParamExcludeFileExtension.length) {
      urlSearch.value['excludeFileExt'] = queryParamExcludeFileExtension;
    } else {
      delete urlSearch.value['excludeFileExt'];
    }
  };

  const parseUrlSearchAndLoadData = async (query: LocationQuery): Promise<void> => {
    if (isLoadData.value) return;

    urlSearch.value['q'] = <string>query['q'];
    setPatternFilterSearch(urlSearch.value['q']);

    urlSearch.value['repo'] = <string>query['repo'];
    if (query['includeFileExt']?.length) {
      urlSearch.value['includeFileExt'] = <string>query['includeFileExt'];
    }
    if (query['excludeFileExt']?.length) {
      urlSearch.value['excludeFileExt'] = <string>query['excludeFileExt'];
    }

    urlSearch.value['repo'].split('+').forEach((gitRepo) => {
      const gitRepoGroupAndProject = gitRepo.split('_');
      setGitRepositoryFilterSearch({
        group: gitRepoGroupAndProject[0],
        project: gitRepoGroupAndProject[1]
      });
      setFilterByRepository(`${gitRepoGroupAndProject[0]}/${gitRepoGroupAndProject[1]}`, true);
    });

    if (urlSearch.value['includeFileExt'] && urlSearch.value['includeFileExt'].length) {
      urlSearch.value['includeFileExt'].split(',').forEach((includeFileExtension: string): void => {
        addIncludeExtensionFileForFilter(includeFileExtension);
      });
    }
    if (urlSearch.value['excludeFileExt'] && urlSearch.value['excludeFileExt'].length) {
      urlSearch.value['excludeFileExt'].split(',').forEach((excludeFileExtension: string): void => {
        addExcludeExtensionFileForFilter(excludeFileExtension);
      });
    }

    await searchByGrep();
  };

  const setActiveShowFile = (activeFile: IGetFileFromGitRepository, language: string): void => {
    activeShowFile.value.file = activeFile;
    activeShowFile.value.language = language;
  };

  const setPatternForIncludeFileForFilter = (pattern: string): void => {
    filtersForSearch.value.filters.patternForIncludeFile = pattern;
  };

  const setPatternForExcludeFileForFilter = (pattern: string): void => {
    filtersForSearch.value.filters.patternForExcludeFile = pattern;
  };

  const setContextForFilter = (context: any): void => {
    filtersForSearch.value.filters.context = parseInt(String(context));
  };

  const setContextBeforeForFilter = (contextBefore: any): void => {
    filtersForSearch.value.filters.contextBefore = parseInt(String(contextBefore));
  };

  const setContextAfterForFilter = (contextAfter: any): void => {
    filtersForSearch.value.filters.contextAfter = parseInt(String(contextAfter));
  };

  const setMaxDepthForFilter = (maxDepth: any): void => {
    filtersForSearch.value.filters.maxDepth = parseInt(String(maxDepth));
  };

  const setMaxCountForFilter = (maxCount: any): void => {
    filtersForSearch.value.filters.maxCount = parseInt(String(maxCount));
  };

  const getRepositoryById = (id: number): IGitRepository | undefined => {
    return gitRepositories.value.find((gitRepository) => gitRepository.id === id);
  };

  const getRepositoryByGroupAndProject = (
    group: string,
    project: string
  ): IGitRepository | undefined => {
    return gitRepositories.value.find((gitRepository) => {
      return gitRepository.group_ === group && gitRepository.project === project;
    });
  };

  const setAllFiltersForSearch = (filter: IFilterSearchGitRepository): void => {
    filter.filters.git.forEach((gitRepo: IControlGitRepository): void => {
      setGitRepositoryFilterSearch(gitRepo);
      setFilterByRepository(`${gitRepo.group}/${gitRepo.project}`, true);
    });

    filter.filters.includeExtensionFiles.forEach((includeExtensionFile: string): void => {
      addIncludeExtensionFileForFilter(includeExtensionFile);
    });
    filter.filters.excludeExtensionFiles.forEach((excludeExtensionFile: string): void => {
      addExcludeExtensionFileForFilter(excludeExtensionFile);
    });

    setPatternFilterSearch(filter.pattern);
    setPatternForIncludeFileForFilter(filter.filters.patternForIncludeFile);
    setPatternForExcludeFileForFilter(filter.filters.patternForExcludeFile);

    setPatternForIncludeFileForFilter(filter.filters.patternForIncludeFile);
    setPatternForExcludeFileForFilter(filter.filters.patternForExcludeFile);
    setContextForFilter(filter.filters.context);
    setContextBeforeForFilter(filter.filters.contextBefore);
    setContextAfterForFilter(filter.filters.contextAfter);
    setMaxDepthForFilter(filter.filters.maxDepth);
    setMaxCountForFilter(filter.filters.maxCount);
  };

  const checkExistsRepoBy_Host_Group_Project = (
    host: string,
    group: string,
    project: string
  ): boolean => {
    const gitRepo: IGitRepository | undefined = gitRepositories.value.find((gitRepo) => {
      return gitRepo.host === host && gitRepo.group_ === group && gitRepo.project === project;
    });
    return gitRepo !== undefined;
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
    updateGitRepositoryOperationFetch,
    parseUrlSearchAndLoadData,
    setActiveShowFile,
    setPatternForIncludeFileForFilter,
    setPatternForExcludeFileForFilter,
    setContextForFilter,
    setContextBeforeForFilter,
    setContextAfterForFilter,
    setMaxDepthForFilter,
    setMaxCountForFilter,
    getRepositoryById,
    getRepositoryByGroupAndProject,
    setAllFiltersForSearch,
    resetAllFilters,
    checkExistsRepoBy_Host_Group_Project,

    gitRepositories,
    resultSearchByGrepGitRepositories,
    isLoading,
    isLoadData,
    isLoadingSearch,
    filtersForSearch,
    filtersByExtensionFiles,
    filtersByRepository,
    stateFormAddGitRepositoryErrors,
    urlSearch,
    activeShowFile
  };
});
