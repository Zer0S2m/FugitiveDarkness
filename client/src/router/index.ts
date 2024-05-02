import { createRouter, createWebHistory } from 'vue-router';
import GitSearchView from '@/views/git/GitSearchView.vue';
import GitProvidersView from '@/views/git/GitProvidersView.vue';
import GitShowFile from '@/views/git/GitShowFile.vue';
import GitRepositoriesView from '@/views/git/GitRepositoriesView.vue';
import GitMatcherNotesView from '@/views/git/GitMatcherNotesView.vue';
import GitJobs from '@/views/git/GitJobs.vue';
import ProjectDetailView from '@/views/project/ProjectDetailView.vue';
import ProjectHomeView from '@/views/project/ProjectHomeView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/git-search',
      name: 'git-search',
      meta: {
        module: 'git'
      },
      component: () => GitSearchView
    },
    {
      path: '/git-repositories',
      name: 'git-repositories',
      meta: {
        module: 'git'
      },
      component: () => GitRepositoriesView
    },
    {
      path: '/git-providers',
      name: 'git-providers',
      meta: {
        module: 'git'
      },
      component: () => GitProvidersView
    },
    {
      path: '/git-file',
      name: 'git-show-file-from-git',
      meta: {
        module: 'git'
      },
      component: () => GitShowFile
    },
    {
      path: '/git-matcher-notes',
      name: 'git-matcher-notes',
      meta: {
        module: 'git'
      },
      component: () => GitMatcherNotesView
    },
    {
      path: '/git-jobs',
      name: 'git-jobs',
      meta: {
        module: 'git'
      },
      component: () => GitJobs
    },
    {
      path: '/project',
      name: 'project-home',
      meta: {
        module: 'project'
      },
      component: () => ProjectHomeView
    },
    {
      path: '/project/:id',
      name: 'project-detail',
      meta: {
        module: 'project'
      },
      component: () => ProjectDetailView
    }
  ]
});

export default router;
