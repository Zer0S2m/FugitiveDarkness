import { createRouter, createWebHistory } from 'vue-router';
import SearchView from '@/views/SearchView.vue';
import GitProvidersView from '@/views/GitProvidersView.vue';
import RepositoriesView from '@/views/GitRepositoriesView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/search',
      name: 'search',
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
    }
  ]
});

export default router;
