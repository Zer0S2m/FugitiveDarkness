<template>
  <ContainerCardItem>
    <div>
      <h4 class="git-item__title">
        <a
          :href="item.link"
          target="_blank"
          >{{ item.project }}</a
        >
      </h4>
    </div>
  </ContainerCardItem>
</template>

<script setup lang="ts">
import ContainerCardItem from '@/components/common/ContainerCardItem.vue';
import type { IGitRepositoryInProvider } from '@/types/gitProvider';

defineProps<{ item: IGitRepositoryInProvider }>();
</script>

<style scoped>
.git-item__title {
  font-size: 18px;
  font-weight: 600;
}

.git-item__title > a {
  color: var(--vt-c-black);
}
</style>
