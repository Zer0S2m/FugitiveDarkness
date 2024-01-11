<template>
  <div class="block-item">
    <div class="block-item__wrapper">
      <slot />
    </div>
  </div>
</template>

<style scoped>
.block-item {
  border: 1px solid var(--color-border);
  width: 100%;
  margin: 4px;
  border-radius: 4px;
}

.block-item__wrapper {
  padding: 16px 12px;
  display: flex;
  justify-content: space-between;
}
</style>
