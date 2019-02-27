<template>
    <div class="header">
        <div class="header__logo">
            <img class="logo" src="@/images/logo1x.png"/>
        </div>
        <div class="header-right">
            <template v-if="loginData">
                <div class="header-switch-app">
                    <span v-if="app.appName">当前正在管理的应用是：</span>
                    <Select v-model="appId" @on-change="handelChangeApp">
                        <Option v-for="app in appList" :value="app.appId" :key="app.appId" :label="app.appName">
                            <span>{{ app.appName }}</span>
                            <span style="float:right;color:#ccc;max-width:100px;overflow: hidden">{{app.tenantAppId}}</span>
                        </Option>
                    </Select>
                    <Button v-if="app.appName" type='primary' icon="ios-copy-outline" size="small" @click="handelCopyAppId">
                        复制AppId
                    </Button>
                </div>
                <Dropdown @on-click="dropdownClicked" trigger="click" style="margin-left: 20px;text-align: left">
                    <a href="javascript:void(0)">
                        {{hasValue(loginData.nickName) ? loginData.nickName : loginData.username}}
                        <Icon type="ios-arrow-down" />
                    </a>
                    <DropdownMenu slot="list">
                        <DropdownItem name="setting">设置</DropdownItem>
                        <DropdownItem name="changePassword">修改密码</DropdownItem>
                        <DropdownItem name="logout">退出登录</DropdownItem>
                    </DropdownMenu>
                </Dropdown>
            </template>
            <a v-else @click="goToLogin">
                登录
            </a>
        </div>
        <Modal v-model="inSettingUser" title="用户修改" class="user-modal">
            <Form :model="settingForm" ref="settingForm" :rules="settingFormRule">
                <FormItem label="昵称:" prop="nickName">
                    <Input v-model="settingForm.nickName" placeholder="请输入用户昵称"></Input>
                </FormItem>
                <FormItem align="right">
                    <Button type="primary" @click="handleSubmit('settingForm')">保存修改</Button>
                </FormItem>
            </Form>
            <div slot="footer"></div>
        </Modal>
        <Modal v-model="inChangingPassword" title="修改密码" class="user-modal">
            <Form :model="changingForm" ref="changingForm" :rules="changingFormRule">
                <FormItem label="旧密码:" prop="oldPassword">
                    <Input v-model="changingForm.oldPassword" placeholder="请输入旧密码" type="password"></Input>
                </FormItem>
                <FormItem label="新密码:" prop="password">
                    <Input v-model="changingForm.password" placeholder="请输入新密码" type="password"></Input>
                </FormItem>
                <FormItem label="确认密码:" prop="passwordConfirm">
                    <Input v-model="changingForm.passwordConfirm" placeholder="请输入确认密码" type="password"></Input>
                </FormItem>
                <FormItem align="right">
                    <Button type="primary" @click="handleChangePasswordSubmit('changingForm')">修改密码</Button>
                </FormItem>
            </Form>
            <div slot="footer"></div>
        </Modal>
    </div>
</template>
<script>
import { setLogout, setUserNickName, getUser, switchApp, getApp, getAppId, getApps } from '@/libs/account';
import { http, hasValue } from '@/libs/util';
import md5 from 'js-md5';

export default {
    data () {
        const validateInput = (rule, value, callback) => {
            if (value !== value.trim()) {
                callback(new Error('请移除前后空格'));
            } else {
                callback();
            }
        };
        return {
            loginData: {},
            app: {},
            appId: '',
            appList: [],
            inSettingUser: false,
            inChangingPassword: false,
            settingForm: {
                nickName: ''
            },
            changingForm: {
                oldPassword: '',
                password: '',
                passwordConfirm: ''
            },
            settingFormRule: {
                nickName: [
                    { required: true, message: '请输入用户昵称', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' },
                    { required: true, type: 'string', min: 2, max: 20, message: '昵称应为 2-20 个字符' },
                    { required: true, pattern: /^[-_A-Za-z0-9\u4E00-\u9FA5]*$/g, message: '用户昵称中只允许包含字母、数字、中文、中划线-和下划线_', trigger: 'blur' }
                ]
            },
            changingFormRule: {
                oldPassword: [
                    { required: true, message: '请输入旧密码', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' },
                    { required: true, type: 'string', min: 6, max: 32, message: '密码应为 6-32 个字符' },
                    { required: true, pattern: /^[-_A-Za-z0-9\u4E00-\u9FA5]*$/g, message: '密码只允许包含字母、数字、中文、中划线-和下划线_', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' },
                    { required: true, type: 'string', min: 6, max: 32, message: '密码应为 6-32 个字符' },
                    { required: true, pattern: /^[-_A-Za-z0-9\u4E00-\u9FA5]*$/g, message: '密码只允许包含字母、数字、中文、中划线-和下划线_', trigger: 'blur' }
                ],
                passwordConfirm: [
                    { required: true, message: '请输入确认密码', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' },
                    { required: true, type: 'string', min: 6, max: 32, message: '密码应为 6-32 个字符' },
                    { required: true, pattern: /^[-_A-Za-z0-9\u4E00-\u9FA5]*$/g, message: '密码只允许包含字母、数字、中文、中划线-和下划线_', trigger: 'blur' }
                ]
            }
        };
    },
    async created () {
        let apps = await getApps();

        if (apps) {
            this.appList = apps;
        }

        let app = getApp();
        if (app) {
            this.app = app;
            this.appId = app.appId;
        }

        this.loginData = getUser();
        this.settingForm.nickName = this.loginData.nickName;
    },
    methods: {
        hasValue,
        handelChangeApp (appId) {
            if (appId === getAppId()) return false;
            let app = this.appList.find(e => {
                return e.appId === appId;
            });

            // 如果当前没有选择 app 就不需要询问是否切换
            if (getApp()) {
                this.$Modal.confirm({
                    title: '你确定要切换吗？',
                    content: '切换 App 将导致当前操作被中断!',
                    onOk: () => {
                        switchApp(app);
                    },
                    onCancel: () => {
                        let app = getApp();
                        this.appId = app.appId;
                    }
                });
            } else {
                switchApp(app);
            }
        },
        handelLogout () {
            this.loginData = null;
            setLogout();
            this.$router.push('/');
            location.reload();
        },
        dropdownClicked: function (name) {
            if (name === 'logout') {
                this.handelLogout();
            } else if (name === 'setting') {
                this.settingForm.nickName = this.loginData.nickName;
                this.inSettingUser = true;
            } else if (name === 'changePassword') {
                this.inChangingPassword = true;
            }
        },
        toIndex: function () {
            this.$router.push('/');
        },
        goToLogin: function () {
            this.$router.push('/login');
        },
        handelCopyAppId: function () {
            let app = getApp();
            this.$copyText(app.tenantAppId).then(() => {
                this.$Message.success('已复制AppId[ ' + app.tenantAppId + ' ]到剪贴板');
            }, () => {
                this.$Message.error('复制失败');
            });
        },
        handleSubmit (name) {
        	console.log(name);
            this.$refs[name].validate(async valid => {
                if (!valid) {
                    this.$Message.error('请先完成所有设置!');
                    return false;
                }

                let response = await http.put(`/user/update/${this.settingForm.nickName}`);
                if (response.data.code !== 200) {
                    this.$Notice.error({
                        title: '错误',
                        desc: response.data.message
                    });
                } else {
                    setUserNickName(this.settingForm.nickName);
                    this.inSettingUser = false;
                    location.reload();
                }
            });
        },
        handleChangePasswordSubmit (name) {
            console.log(name);
            this.$refs[name].validate(async valid => {
                if (!valid) {
                    this.$Message.error('请先完成所有设置!');
                    return false;
                }

                let response = await http.post(`/user/changePassword`, {
                    oldPassword: md5(this.changingForm.oldPassword),
                    password: md5(this.changingForm.password),
                    passwordConfirm: md5(this.changingForm.passwordConfirm)
                });
                if (response.data.code !== 200) {
                    this.$Notice.error({
                        title: '错误',
                        desc: response.data.message
                    });
                } else {
                    this.$Notice.success({
                        title: '成功',
                        desc: "密码已经修改，请重新登录"
                    });
                    this.inChangingPassword = false;
                    this.handelLogout();
                }
            });
        }
    }
};
</script>
<style scoped lang="scss">
    .header {
        height: 64px;
        padding: 0 20px;
        display: flex;
        justify-content: space-between;
        align-self: center;
        font-size: 12px;
        line-height: 64px;
        background-color: #fff;
        box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.05);
        .header__logo {
            height: 100%;
            .logo {
                display: block;
                height: 100%;
                width: auto;
            }
        }
        .header-right, .header-switch-app {
            display: flex;
            align-items: center;
            & > span {
                white-space: nowrap;
                text-align: right;
            }
        }
    }
</style>
<style lang="scss">
    .header-switch-app {
        .ivu-select-selection {
            border: 0;
        }
        .ivu-select-visible .ivu-select-selection {
            box-shadow: none;
        }
        .ivu-select-dropdown {
            width: 240px !important;
        }
    }
    .user-modal{
        .ivu-modal {
            width: 80% !important;
            max-width: 600px;
        }
        .ivu-modal-footer {
            display: none;
        }
    }
</style>
