import axios from 'axios';
import { Notice } from 'iview';
import Vue from 'vue';
import { hasObject, hasValue } from './util';
import { clearCurrentApp, getAppId, getUser, appId } from './account';

const ajax = axios.create({
    baseURL: process.env.VUE_APP_HTTP_API,
    timeout: process.env.VUE_APP_HTTP_TIMEOUT
});

ajax.interceptors.request.use(config => {
    let user = getUser();
    let appId = getAppId();
    if (hasValue(user.token)) {
        config.headers = {
            'Authorization': 'Bearer ' + user.token,
            appId: hasValue(appId) ? appId : 0
        };
    }
    return config;
}, error => {
    return Promise.reject(error);
});

ajax.interceptors.response.use(response => {
    if (response.data.code === 1002 || response.data.code === 1008) {
        Vue.$router.push({
            name: 'login'
        });
    } else if (response.data.code === 1003) {
        Notice.error({
            title: '错误',
            desc: response.data.message
        });
        clearCurrentApp();
        Vue.$router.push({
            name: 'switch-app'
        });
    }
    return response;
}, error => {
    Notice.error({
        title: '网络错误,请稍后再试',
        desc: error.message
    });
    return Promise.reject(error);
});

export default ajax;
