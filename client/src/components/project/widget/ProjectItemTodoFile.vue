<script setup lang="ts">
import {
  type IMatcherFoundByGrepGitRepository,
  type ISearchFoundByGrepGitRepository
} from '@/types/gitRepository';
import { HalfCircleSpinner } from 'epic-spinners';
import Code from '@/components/common/Code.vue';

defineProps<{
  isLoading: boolean;
  todo: ISearchFoundByGrepGitRepository[];
}>();

const collectCode = (todoItem: ISearchFoundByGrepGitRepository): string => {
  let code: string = '';

  todoItem.matchers.forEach((matcher: IMatcherFoundByGrepGitRepository): void => {
    code += `${matcher.matcher}\n`;
  });

  return code;
};

const collectLineNumbers = (todoItem: ISearchFoundByGrepGitRepository): number[] => {
  let lineNumbers: number[] = [];

  todoItem.matchers.forEach((matcher: IMatcherFoundByGrepGitRepository): void => {
    lineNumbers.push(matcher.lineNumber);
  });

  return lineNumbers;
};
</script>

<template>
  <div class="todo">
    <div class="todo--wrapper">
      <h4 class="todo--title">Todos</h4>
      <div
        class="todo--loader"
        v-if="isLoading"
      >
        <HalfCircleSpinner
          :animation-duration="1000"
          :size="32"
          color="var(--color-secondary)"
        />
      </div>
      <div
        class="todo--code"
        v-if="todo.length && !isLoading"
      >
        <ul class="todo-code--items">
          <li
            class="todo-code--item"
            v-for="todoItem in todo"
          >
            <h5 class="todo-code-item--title">{{ todoItem.filename }}</h5>
            <Code
              :code="collectCode(todoItem)"
              :language="todoItem.extension"
              :line-numbers="collectLineNumbers(todoItem)"
            />
          </li>
        </ul>
      </div>
      <div
        class="todo--no"
        v-else-if="!todo.length && !isLoading"
      >
        <h5 class="todo-no--title">Todo not find</h5>
      </div>
    </div>
  </div>
</template>

<style scoped>
.todo {
  border-radius: 4px;
  border: 1px solid var(--color-secondary);
}

.todo--wrapper {
  padding: 8px;
  overflow: hidden;
}

.todo--title {
  font-size: 18px;
  font-weight: 600;
}

.todo--loader {
  margin-top: 8px;
  display: flex;
  justify-content: center;
}

.todo--code {
  margin-top: 12px;
}

.todo-code--item {
  margin-bottom: 8px;
}
.todo-code--item:last-child {
  margin-bottom: 0;
}

.todo-code-item--title {
  margin-bottom: 4px;
}

.todo--no {
  margin-top: 8px;
}
</style>
