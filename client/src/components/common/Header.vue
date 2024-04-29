<script setup lang="ts">
import { RouterLink, useRouter } from 'vue-router';
import SelectModule from '@/components/common/SelectModule.vue';
import { useHeaderState } from '@/stores/useHeaderState';
import { onMounted } from 'vue';

const useHeaderStore = useHeaderState();

const movingModule = async (module: string): Promise<void> => {
  await useHeaderStore.changePageByActiveModule(module);
  useHeaderStore.setActiveModule(module);
};

onMounted(async (): Promise<void> => {
  const vueRouterStore = useRouter();
  await vueRouterStore.isReady();

  if (vueRouterStore.currentRoute.value.meta.module) {
    // @ts-ignore
    useHeaderStore.setActiveModule(vueRouterStore.currentRoute.value.meta.module);
  }
});
</script>

<template>
  <header class="nav-header">
    <div class="nav-header__wrapper">
      <div class="nav-header-block-logo">
        <span>FD</span>
      </div>
      <nav class="nav-items">
        <div
          :class="'nav-item'"
          v-for="infoRoute in useHeaderStore.getInfoRoutersByActiveModule(
            useHeaderStore.activeModule
          )"
        >
          <div class="nav-item__wrapper">
            <RouterLink
              class="nav-item__link"
              :to="{ name: infoRoute.nameRouter }"
              >{{ infoRoute.titleRouter }}
            </RouterLink>
          </div>
        </div>
      </nav>
      <div class="nav__header--module">
        <SelectModule
          :selected="useHeaderStore.activeModule"
          @moving-module="movingModule"
        />
      </div>
    </div>
  </header>
</template>

<style scoped>
.nav-header {
  width: 100%;
  height: var(--height-header);
  position: sticky;
  top: 0;
  background-color: var(--color-background);
  z-index: 1;
}

.nav-header__wrapper {
  display: flex;
  height: 100%;
  border-bottom: 1px solid var(--color-border);
}

.nav-header-block-logo {
  width: 54px;
}

.nav-header-block-logo > span {
  display: flex;
  width: 100%;
  height: 100%;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
}

.nav-items {
  width: 100%;
  display: flex;
}

.nav-item {
  border-left: 1px solid var(--color-border);
}
.nav-item:last-child {
  border-right: 1px solid var(--color-border);
}

.nav-item__wrapper {
  display: flex;
  align-content: center;
  justify-content: center;
  height: 100%;
}

.nav-item__link {
  align-items: center;
  justify-content: center;
  display: flex;
  padding: 16px;
  color: var(--color-text);
}

.nav-item__link.router-link-active {
  color: var(--color-secondary);
}

.nav__header--module {
  display: flex;
  align-items: center;
  margin: 0 20px;
}
</style>
