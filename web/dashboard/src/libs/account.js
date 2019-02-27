import { ajax, hasObject } from './util';
import http from './http';
import Vue from 'vue';

/**
 * 当前登录用户是否为管理员
 * @returns {boolean}
 */
export const isAdmin = () => {
    return localStorage.getItem('admin') === 'true';
};

export const isLogin = () => {
    return getUser() !== false;
};

/**
 * 设置当前用户身份
 * @param isAdmin
 */
export const setAdmin = (isAdmin) => {
    localStorage.setItem('admin', isAdmin);
};

/**
 * 设置登录状态
 */
export const setLogin = data => {
    localStorage.setItem('loginData', JSON.stringify(data));
};

/**
 * 设置退出状态
 */
export const setLogout = () => {
    localStorage.clear();
};

export const setUserNickName = nickName => {
    let user = getUser();
    user.nickName = nickName;
    setLogin(user);
};

/**
 * 获取当前登录用户信息
 * @returns {*}
 */
export const getUser = () => {
    return getLocalStoreage('loginData');
};

/**
 * 获取用户选择的 App
 * @returns {boolean|*}
 */
export const getApp = () => {
    return getLocalStoreage('app');
};

export const getApps = async () => {
    let user = getUser();
    if (user === false) return false;
    let response = await http.get('/app/bind');

    return response.data.code === 200 && response.data.data.length > 0 ? response.data.data : false;
};

export const getAppId = () => {
    let app = getApp();
    return app ? app.appId : false;
};

const getLocalStoreage = (key) => {
    let data = localStorage.getItem(key);
    if (!data) return false;

    try {
        JSON.parse(data);
        return JSON.parse(data);
    } catch (e) {
        return false;
    }
};

/**
 * 切换 app
 * @param app
 */
export const switchApp = (app) => {
    localStorage.setItem('app', JSON.stringify(app));
    localStorage.setItem('appId', app.appId);

    // 部分带参数的页面，需要返回上一级再刷新
    if (hasObject('history.current.meta.refresh', Vue.$router)) {
        Vue.$router.push(Vue.$router.history.current.meta.refresh);
        setTimeout(() => {
            location.reload();
        });
    } else {
        location.reload();
    }
};

export const clearCurrentApp = () => {
    localStorage.removeItem('app');
    localStorage.removeItem('appId');
};

export default {
    isAdmin,
    setAdmin,
    setLogin,
    setLogout,
    getUser,
    getApp,
    getApps,
    clearCurrentApp,
    isLogin
};
