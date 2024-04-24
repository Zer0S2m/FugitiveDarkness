<template>
  <div class="block-items">
    <slot />
  </div>
</template>

<style scoped>
.block-items {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-column-gap: 8px;
  grid-row-gap: 8px;
}

@media screen and (min-width: 1024px) {
  .block-items {
    grid-template-columns: repeat(5, 1fr);
  }
}
</style>
