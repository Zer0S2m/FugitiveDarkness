import { createRouter, createWebHistory } from 'vue-router';
import SearchView from '@/views/SearchView.vue';
import RepositoriesView from '@/views/RepositoriesView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'search',
      component: () => SearchView
    },
    {
      path: '/git-repositories',
      name: 'git-repositories',
      component: () => RepositoriesView
    }
  ]
});

export default router;
