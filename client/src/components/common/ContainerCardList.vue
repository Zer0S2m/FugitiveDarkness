<template>
  <div class="block-items">
    <slot />
  </div>
</template>

<style scoped>
.block-items {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-column-gap: 8px;
  grid-row-gap: 2px;
}
</style>
