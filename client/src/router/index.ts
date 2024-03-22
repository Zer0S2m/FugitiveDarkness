import { createRouter, createWebHistory } from 'vue-router';
import SearchView from '@/views/SearchView.vue';
import GitProvidersView from '@/views/GitProvidersView.vue';
import GitShowFile from '@/views/GitShowFile.vue';
import RepositoriesView from '@/views/GitRepositoriesView.vue';
import MatcherNotesView from '@/views/MatcherNotesView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/git-search',
      name: 'git-search',
      component: () => SearchView
    },
    {
      path: '/git-repositories',
      name: 'git-repositories',
      component: () => RepositoriesView
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
      component: () => MatcherNotesView
    }
  ]
});

export default router;
