import { h, defineComponent } from 'vue';
import hljs from 'highlight.js/lib/common';
import { searchMatch } from '@/utils/search';

export default defineComponent({
  props: {
    code: {
      type: String
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
    }
  },
  setup(props, { slots, attrs }) {
    let language = props.language;
    let highlightedCode: string;

    try {
      highlightedCode = hljs.highlight(props.code as string, {
        language
      }).value;
    } catch (e) {
      highlightedCode = hljs.highlight(props.code as string, {
        language: 'plaintext'
      }).value;
      language = 'plaintext';
    }
    const className = `language-${language}`;

    if (props.pattern.length !== 0) {
      highlightedCode = searchMatch(highlightedCode, props.pattern);
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
