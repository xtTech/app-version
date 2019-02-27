import Vue from 'vue';
import iView from 'iview';
import router from './router';
import App from './app.vue';
import 'iview/dist/styles/iview.css';
import '@/styles/reset.css';
import '@fortawesome/fontawesome-free/css/all.css';
import VueClipboard from 'vue-clipboard2';

Vue.use(iView);
Vue.use(VueClipboard);

Vue.$router = Vue.prototype.router = router;

new Vue({
    el: '#app',
    router: router,
    render: h => h(App)
});
