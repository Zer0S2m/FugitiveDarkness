import { defineStore } from 'pinia';
import { useRouter } from 'vue-router';
import { type Ref, ref } from 'vue';

export const useHeaderState = defineStore('header', () => {
  const vueRouterStore = useRouter();

  const modules: {
    title: string;
    routers: {
      nameRouter: string;
      titleRouter: string;
    }[];
  }[] = [
    {
      title: 'git',
      routers: [
        {
          nameRouter: 'git-search',
          titleRouter: 'Search'
        },
        {
          nameRouter: 'git-repositories',
          titleRouter: 'Repositories'
        },
        {
          nameRouter: 'git-providers',
          titleRouter: 'Providers'
        },
        {
          nameRouter: 'git-matcher-notes',
          titleRouter: 'Notes matcher'
        },
        {
          nameRouter: 'git-jobs',
          titleRouter: 'Jobs'
        }
      ]
    },
    {
      title: 'project',
      routers: [
        {
          nameRouter: 'project-home',
          titleRouter: 'Git projects'
        }
      ]
    }
  ];
  const activeModule: Ref<string> = ref('git');

  const setActiveModule = (moduleTitle: string): void => {
    activeModule.value = moduleTitle;
  };

  const getInfoRoutersByActiveModule = (
    module: string
  ): {
    nameRouter: string;
    titleRouter: string;
  }[] => {
    const activeModule:
      | {
          title: string;
          routers: {
            nameRouter: string;
            titleRouter: string;
          }[];
        }
      | undefined = modules.find(
      (infoRoute: {
        title: string;
        routers: {
          nameRouter: string;
          titleRouter: string;
        }[];
      }): boolean => {
        return infoRoute.title === module;
      }
    );

    if (activeModule) {
      return activeModule.routers;
    }

    return [];
  };

  const changePageByActiveModule = async (module: string): Promise<void> => {
    if (module === activeModule.value) {
      return;
    }

    if (module === 'git') {
      await vueRouterStore.push({ path: '/git-search' });
    } else if (module === 'project') {
      await vueRouterStore.push({ path: '/project' });
    }
  };

  return {
    activeModule,

    setActiveModule,
    getInfoRoutersByActiveModule,
    changePageByActiveModule
  };
});
