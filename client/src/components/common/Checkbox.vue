<template>
  <div class="checkbox-wrapper">
    <label class="control control--checkbox">
      {{ title }}
      <input
        v-bind:class="{
          activity: isActivity
        }"
        type="checkbox"
        @:click="clickHandler(meta, !isActivity)"
      />
      <div class="control__indicator"></div>
    </label>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string;
  clickHandler: Function;
  meta: any;
  isActivity: boolean;
}>();
</script>

<style scoped>
.checkbox-wrapper .control {
  position: relative;
  padding-left: 22px;
  cursor: pointer;
  font-size: 14px;
}

.checkbox-wrapper .control input {
  position: absolute;
  z-index: -1;
  opacity: 0;
}

.checkbox-wrapper .control__indicator {
  position: absolute;
  top: 2px;
  left: 0;
  height: 16px;
  width: 16px;
  background: #e6e6e6;
}

.checkbox-wrapper .control:hover input ~ .control__indicator,
.checkbox-wrapper .control input:focus ~ .control__indicator {
  background: #ccc;
}

.checkbox-wrapper .control input.activity ~ .control__indicator {
  background: var(--color-secondary);
}

.checkbox-wrapper .control input:disabled ~ .control__indicator {
  background: #e6e6e6;
  opacity: 0.6;
  pointer-events: none;
}

.checkbox-wrapper .control__indicator:after {
  content: '';
  position: absolute;
  display: none;
}

.checkbox-wrapper .control input.activity ~ .control__indicator:after {
  display: block;
}

.checkbox-wrapper .control--checkbox .control__indicator:after {
  left: 6px;
  top: 3px;
  width: 4px;
  height: 8px;
  border: solid #fff;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.checkbox-wrapper .control--checkbox input:disabled ~ .control__indicator:after {
  border-color: #7b7b7b;
}
</style>
