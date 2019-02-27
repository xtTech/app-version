import Vue from 'vue';
import iView from 'iview';
import VueRouter from 'vue-router';
import { updateSiteTitle } from './libs/util';
import Entry from './views/entry';
Vue.use(iView);
Vue.use(VueRouter);

const router = new VueRouter({
    base: '/',
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'home',
            meta: {
                title: ''
            },
            redirect: {
                name: 'switch-app'
            },
            component: Entry,
            children: [
                {
                    path: 'app',
                    name: 'switch-app',
                    meta: {
                        title: '选择应用'
                    },
                    component: (resolve) => require(['./views/home/home.vue'], resolve)
                }
            ]
        },
        {
            path: '/admin',
            name: 'admin',
            meta: {
                title: '管理'
            },
            component: Entry,
            redirect: {
                name: 'admin-app'
            },
            children: [
                {
                    path: 'app',
                    name: 'admin-app',
                    meta: {
                        title: '应用管理'
                    },
                    component: (resolve) => require(['./views/admin/app.vue'], resolve)
                },
                {
                    path: 'user',
                    name: 'admin-user',
                    meta: {
                        title: '用户列表'
                    },
                    component: (resolve) => require(['./views/admin/user.vue'], resolve)
                },
                {
                    path: 'log',
                    name: 'admin-log',
                    meta: {
                        title: '操作日志'
                    },
                    component: (resolve) => require(['./views/admin/log.vue'], resolve)
                }
            ]
        },
        {
            path: '/version',
            name: 'version',
            meta: {
                title: '版本管理'
            },
            redirect: {
                name: 'version-channel'
            },
            component: Entry,
            children: [
                {
                    path: 'channel',
                    name: 'version-channel',
                    meta: {
                        title: '渠道列表'
                    },
                    component: (resolve) => require(['./views/version/channel/index.vue'], resolve)
                },
                {
                    path: 'ios',
                    name: 'version-ios-redirect',
                    meta: {
                        title: 'iOS 版本管理'
                    },
                    redirect: {
                        name: 'version-ios'
                    },
                    component: (resolve) => require(['./views/version/ios/index.vue'], resolve),
                    children: [
                        {
                            path: 'list',
                            name: 'version-ios',
                            meta: {
                                title: '',
                                pageTitle: 'iOS 版本管理'
                            },
                            component: (resolve) => require(['./views/version/ios/list.vue'], resolve)
                        },
                        {
                            path: 'create',
                            name: 'version-ios_create',
                            meta: {
                                title: '添加',
                                pageTitle: 'iOS 版本添加'
                            },
                            component: (resolve) => require(['./views/version/ios/edit.vue'], resolve)
                        },
                        {
                            path: 'edit/:iOSId',
                            name: 'version-ios_edit',
                            meta: {
                                refresh: {
                                    name: 'version-ios'
                                },
                                title: '编辑',
                                pageTitle: 'iOS 版本编辑'
                            },
                            component: (resolve) => require(['./views/version/ios/edit.vue'], resolve)
                        }
                    ]
                },
                {
                    path: 'android',
                    name: 'version-android-redirect',
                    meta: {
                        title: 'Android 版本管理'
                    },
                    redirect: {
                        name: 'version-android'
                    },
                    component: (resolve) => require(['./views/version/android/index.vue'], resolve),
                    children: [
                        {
                            path: 'list',
                            name: 'version-android',
                            meta: {
                                title: '',
                                pageTitle: 'Android 版本管理'
                            },
                            component: (resolve) => require(['./views/version/android/list.vue'], resolve)
                        },
                        {
                            path: 'create',
                            name: 'version-android_create',
                            meta: {
                                title: '添加',
                                pageTitle: 'Android 版本添加'
                            },
                            component: (resolve) => require(['./views/version/android/edit.vue'], resolve)
                        },
                        {
                            path: 'edit/:androidId',
                            name: 'version-android_edit',
                            meta: {
                                refresh: {
                                    name: 'version-android'
                                },
                                title: '编辑',
                                pageTitle: 'Android 版本编辑'
                            },
                            component: (resolve) => require(['./views/version/android/edit.vue'], resolve)
                        },
                        {
                            path: 'apk/:androidId',
                            name: 'version-android_apk',
                            meta: {
                                refresh: {
                                    name: 'version-android'
                                },
                                title: '包管理',
                                pageTitle: 'Android 包管理'
                            },
                            component: (resolve) => require(['./views/version/android/apk.vue'], resolve)
                        }
                    ]
                }
            ]
        },
        {
            path: '/rn',
            name: 'rn',
            meta: {
                title: 'RN 管理'
            },
            redirect: {
                name: 'rn-package'
            },
            component: Entry,
            children: [
                {
                    path: 'package',
                    name: 'rn-package',
                    meta: {
                        title: 'RN 包管理'
                    },
                    component: (resolve) => require(['./views/rn/package.vue'], resolve)
                },
                {
                    path: 'router',
                    name: 'rn-router',
                    meta: {
                        title: 'RN 路由管理'
                    },
                    component: (resolve) => require(['./views/rn/router.vue'], resolve)
                }
            ]
        },
        {
            path: '/capi',
            name: 'capi',
            meta: {
                title: '自定义接口'
            },
            redirect: {
                name: 'capi-list'
            },
            component: Entry,
            children: [
                {
                    path: 'list',
                    name: 'capi-list',
                    meta: {
                        title: '',
                        pageTitle: '自定义接口'
                    },
                    component: (resolve) => require(['./views/capi/list.vue'], resolve)
                }
            ]
        },
        {
            path: '/manual',
            name: 'manual',
            meta: {
                title: '操作手册'
            },
            redirect: {
                name: 'manual-android'
            },
            component: Entry,
            children: [
                {
                    path: 'android',
                    name: 'manual-android',
                    meta: {
                        title: 'Android 版本'
                    },
                    component: (resolve) => require(['./views/manual/mAndroid.vue'], resolve)
                },
                {
                    path: 'channel',
                    name: 'manual-channel',
                    meta: {
                        title: '添加渠道'
                    },
                    component: (resolve) => require(['./views/manual/mChannel.vue'], resolve)
                },
                {
                    path: 'ios',
                    name: 'manual-ios',
                    meta: {
                        title: 'iOS版本'
                    },
                    component: (resolve) => require(['./views/manual/mIOS.vue'], resolve)
                },
                {
                    path: 'type',
                    name: 'manual-type',
                    meta: {
                        title: '更新类型说明'
                    },
                    component: (resolve) => require(['./views/manual/mType.vue'], resolve)
                },
                {
                    path: 'package',
                    name: 'manual-package',
                    meta: {
                        title: '添加 RN 包'
                    },
                    component: (resolve) => require(['./views/manual/mPackage.vue'], resolve)
                },
                {
                    path: 'router',
                    name: 'manual-router',
                    meta: {
                        title: '添加 RN 路由'
                    },
                    component: (resolve) => require(['./views/manual/mRouter.vue'], resolve)
                }
            ]
        },
        {
            path: '/login',
            name: 'login',
            meta: {
                title: '登录'
            },
            component: (resolve) => require(['./views/login/index.vue'], resolve)
        },
        {
            path: '/register',
            name: 'register',
            meta: {
                title: '注册'
            },
            component: (resolve) => require(['./views/login/register.vue'], resolve)
        }
    ]
});

router.beforeEach((to, from, next) => {
    if (to.name !== 'switch-app' && to.name !== 'login' && to.name !== 'register') {
        // 检查是否登录
        let loginData = localStorage.getItem('loginData');
        if (!loginData) {
            // 未登录
            router.push({
                name: 'switch-app'
            });
            iView.Notice.error({
                title: '未登录',
                desc: '请先登录！'
            });
            return;
        }
        if (['admin', 'admin-app', 'admin-user', 'admin-log', 'manual-android', 'manual-ios', 'manual-channel', 'manual-router', 'manual-package', 'manual-type'].indexOf(to.name) === -1) {
            let appId = localStorage.getItem('appId');
            if (isNaN(appId) || appId < 1) {
                // 未登录
                router.push({
                    name: 'switch-app'
                });
                iView.Notice.error({
                    title: '未选择应用',
                    desc: '请先选择应用，再进行下一步操作！'
                });
                return;
            }
        }
    }
    iView.LoadingBar.start();
    updateSiteTitle(to.meta.pageTitle != null ? to.meta.pageTitle : to.meta.title);
    next();
});

router.afterEach(() => {
    iView.LoadingBar.finish();
    window.scrollTo(0, 0);
});

export default router;
