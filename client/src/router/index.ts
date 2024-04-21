import { createRouter, createWebHistory } from 'vue-router';
import GitSearchView from '@/views/git/GitSearchView.vue';
import GitProvidersView from '@/views/git/GitProvidersView.vue';
import GitShowFile from '@/views/git/GitShowFile.vue';
import GitRepositoriesView from '@/views/git/GitRepositoriesView.vue';
import GitMatcherNotesView from '@/views/git/GitMatcherNotesView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/git-search',
      name: 'git-search',
      component: () => GitSearchView
    },
    {
      path: '/git-repositories',
      name: 'git-repositories',
      component: () => GitRepositoriesView
    },
    {
      path: '/git-providers',
      name: 'git-providers',
      component: () => GitProvidersView
    },
    {
      path: '/git-file',
      name: 'git-show-file-from-git',
      component: () => GitShowFile
    },
    {
      path: '/matcher-notes',
      name: 'matcher-notes',
      component: () => GitMatcherNotesView
    }
  ]
});

export default router;
