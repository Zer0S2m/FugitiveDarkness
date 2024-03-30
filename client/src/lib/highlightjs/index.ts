import { h, defineComponent, type PropType } from 'vue';
import hljs from 'highlight.js/lib/common';
import { searchMatch } from '@/utils/search';
import type { IMatcherFoundGroupByGrepGitRepository } from '@/types/gitRepository';

export default defineComponent({
  props: {
    code: {
      type: String
    },
    previewLastCode: {
      type: String,
      default: ''
    },
    previewNextCode: {
      type: String,
      default: ''
    },
    language: {
      type: String,
      default: 'markup'
    },
    isUseClass: {
      type: Boolean,
      default: true
    },
    pattern: {
      type: String,
      default: ''
    },
    groups: {
      type: Array as PropType<Array<IMatcherFoundGroupByGrepGitRepository>>,
      default: []
    }
  },
  setup(props, { slots, attrs }) {
    let language = props.language;
    let highlightedCode: string;
    let code: string = '';

    code += `${props.previewLastCode}`;
    code += `${props.code}`;
    code += `${props.previewNextCode}`;

    try {
      highlightedCode = hljs.highlight(code as string, {
        language
      }).value;
    } catch (e) {
      highlightedCode = hljs.highlight(code as string, {
        language: 'plaintext'
      }).value;
      language = 'plaintext';
    }
    const className = `language-${language}`;

    if (props.pattern.length !== 0) {
      highlightedCode = searchMatch(highlightedCode, props.pattern, props.groups);
    }

    return () => {
      return h(
        'pre',
        {
          ...attrs,
          class: props.isUseClass ? [attrs.class, className] : []
        },
        [
          h('code', {
            ...attrs,
            class: props.isUseClass ? [attrs.class, className] : [],
            innerHTML: highlightedCode
          })
        ]
      );
    };
  }
});
