<template>
    <div id="admin-app-edit">
        <Form ref="editFormRule" :model="editForm" :rules="editFormRule" :label-width="120" style="position: relative">
            <FormItem label="应用名称" prop="appName">
                <Input v-model="editForm.appName" placeholder=""/>
            </FormItem>
            <FormItem label="租户 AppId" prop="tenantAppId">
                <Input v-model="editForm.tenantAppId" placeholder=""/>
            </FormItem>
        </Form>
        <Spin size="large" fix v-if="inLoading">
            <Icon type="load-c" size=18 class="spin-icon-load" style="margin-bottom: 12px"></Icon>
            <div>正在加载...</div>
        </Spin>
        <Row class="footer">
            <Col span="24">
                <Button type="text" size="large" @click="handleReset('editFormRule')">取消</Button>
                <Button type="primary" size="large" @click="handleSubmit('editFormRule')">提交</Button>
            </Col>
        </Row>
    </div>
</template>
<script>
import { http } from '@/libs/util';
import { getApp, getApps, switchApp } from '@/libs/account';

export default {
    props: {
        'appId': {
            type: Number
        },
        admin: {
            type: Boolean,
            default : true
        }
    },
    data () {
        const validateInput = (rule, value, callback) => {
            if (value !== value.trim()) {
                callback(new Error('请移除前后空格'));
            } else {
                callback();
            }
        };
        return {
            inLoading: false,
            isEdit: false, // 是否为编辑状态，false 表示创建，true 编辑中
            editFormRule: {
                appName: [
                    { required: true, message: '请输入应用名称', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ],
                tenantAppId: [
                    { required: true, message: '请输入租户 AppId', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ]
            },
            editForm: {
                appName: '',
                tenantAppId: ''
            },
            disabledAppId: ''
        };
    },
    created () {
        if (this.appId > 0) {
            this.isEdit = true;
            this.getApp();
        } else {
            this.inLoading = false;
        }
    },
    methods: {
        async getApp () {
            let response = await http.get(`/admin/app/${this.appId}`);
            if (response.data.code !== 200) {
                this.$Notice.error({
                    title: '错误',
                    desc: response.message
                });
                this.$emit('close-edit-modal');
            } else {
                let data = response.data.data;
                this.disabledAppId = data.id;
                this.editForm = {
                    appName: data.appName,
                    tenantAppId: data.tenantAppId
                };
            }
        },
        async postBot () {
            let query = {
                appName: this.editForm.appName,
                tenantAppId: this.editForm.tenantAppId
            };
            let url = this.admin ? '/admin/app' : '/app';
            let response = await http.post(url, query);
            return response.data;
        },
        async putBot () {
            let query = {
                appName: this.editForm.appName,
                tenantAppId: this.editForm.tenantAppId
            };
            let url = this.admin ? '/admin/app' : '/app';
            let response = await http.put(url + this.appId, query);
            return response.data;
        },
        handleReset (name) {
            this.$refs[name].resetFields();
            this.$emit('close-edit-modal');
        },
        async handleSubmit (name) {
            this.$refs[name].validate((valid) => {
                if (!valid) {
                    this.$Message.error('请先完成所有必填项内容!');
                    return false;
                }

                const message = response => {
                    let eventText = this.isEdit === true ? '编辑' : '添加';
                    if (response.code === 200) {
                        this.$Notice.success({
                            title: '请求成功',
                            desc: `${eventText}应用 ${response.data.appName} 成功`
                        });

                        if (this.isEdit) {
                            let app = getApp();
                            // 如果修改的是当前正在运行的 app, 则更新本地 app 存储并强制刷新页面
                            if (app.appId === this.disabledAppId) {
                                getApps().then(apps => {
                                    switchApp(apps.find(e => {
                                        return e.appId === app.appId;
                                    }));
                                });
                            } else {
                                this.$emit('close-edit-modal');
                            }
                        } else {
                            this.$emit('close-edit-modal');
                        }
                    } else {
                        this.$Notice.error({
                            title: '请求失败',
                            desc: response.message
                        });
                    }
                };

                if (this.isEdit === true) {
                    this.putBot().then(message);
                } else {
                    this.postBot().then(message);
                }
            });
        }
    }
};
</script>
<style scoped lang="scss">
    #admin-app-edit {
        .footer {
            text-align: right;
            border-top: 1px solid #e9eaec;
            padding: 14px 0 0 0;
            line-height: 1;
        }
    }
</style>
