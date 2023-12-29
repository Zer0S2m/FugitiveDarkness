import './assets/main.css';
import 'vue-final-modal/style.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from './App.vue';
import Vueform from '@vueform/vueform';
import vueformConfig from '../vueform.config';
import router from './router';
import { createVfm } from 'vue-final-modal';

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(createVfm());
app.use(Vueform, vueformConfig);

app.mount('#app');
