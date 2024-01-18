<script setup lang="ts">
import { RouterLink } from 'vue-router';
</script>

<template>
  <header class="nav-header">
    <div class="nav-header__wrapper">
      <div class="nav-header-block-logo">
        <span>FD</span>
      </div>
      <nav class="nav-items">
        <div class="nav-item nav-item-1">
          <div class="nav-item__wrapper">
            <RouterLink
              class="nav-item__link"
              :to="{ name: 'search' }"
              >Search</RouterLink
            >
          </div>
        </div>
        <div class="nav-item nav-item-2">
          <div class="nav-item__wrapper">
            <RouterLink
              class="nav-item__link"
              :to="{ name: 'git-repositories' }"
              >Repositories</RouterLink
            >
          </div>
        </div>
        <div class="nav-item nav-item-3">
          <div class="nav-item__wrapper">
            <RouterLink
              class="nav-item__link"
              :to="{ name: 'git-providers' }"
              >Providers</RouterLink
            >
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<style scoped>
.nav-header {
  width: 100%;
  height: 40px;
  position: sticky;
  top: 0;
  background-color: var(--color-background);
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
  align-content: center;
}

.nav-item {
  border-left: 1px solid var(--color-border);
  border-right: 1px solid var(--color-border);
}

.nav-item-1,
.nav-item-2 {
  border-right: 0;
}

.nav-item__wrapper {
  padding: 0 4px;
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
</style>
