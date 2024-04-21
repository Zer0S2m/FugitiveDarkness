<template>
  <ContainerCardList>
    <GitProviderItem
      v-for="item in items"
      :item="item"
    />
    <AddGitItemButton
      @onClick="openModalAddGitProvider"
      class="git-provider__add"
    />
  </ContainerCardList>
</template>

<script setup lang="ts">
import type { IGitProvider } from '@/types/gitProvider';
import ContainerCardList from '@/components/common/ContainerCardList.vue';
import GitProviderItem from '@/components/git/widget/GitProviderItem.vue';
import AddGitItemButton from '@/components/common/AddGitItemButton.vue';

defineProps<{ items: IGitProvider[] }>();
const emit = defineEmits(['openModalAddGitProvider']);

const openModalAddGitProvider = () => {
  emit('openModalAddGitProvider');
};
</script>

<style scoped>
.git-provider__add {
  margin: 4px;
}
</style>
