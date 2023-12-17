import {createRouter, createWebHistory} from 'vue-router'
import MainView from "@/views/MainView.vue";
import RepositoriesView from "@/views/RepositoriesView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: () => MainView
    },
    {
      path: '/git-repositories',
      name: 'git-repositories',
      component: () => RepositoriesView
    }
  ]
})

export default router
