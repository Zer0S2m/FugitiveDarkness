import './assets/main.css';
import 'vue-final-modal/style.css';
import 'highlight.js/styles/atom-one-light.min.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from './App.vue';
import Vueform from '@vueform/vueform';
import vueformConfig from '../vueform.config';
import router from './router';
import { createVfm } from 'vue-final-modal';
import hljs from 'highlight.js';

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(createVfm());
app.use(Vueform, vueformConfig);

app.mount('#app');

hljs.highlightAll();
hljs.registerAliases(['aj'], { languageName: 'java' });
hljs.registerAliases(['flake8', 'factories', 'conf'], { languageName: 'ini' });
hljs.registerAliases(['gitignore', 'in', 'mvnw'], { languageName: 'bash' });
hljs.registerAliases(['LICENSE', 'types', 'rst'], { languageName: 'plaintext' });
hljs.registerAliases(['scss', 'sass'], { languageName: 'css' });
