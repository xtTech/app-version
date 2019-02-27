<template>
    <div id="admin-app-edit">
        <Form ref="editFormRule" :model="editForm" :rules="editFormRule" :label-width="150" style="position: relative">
            <FormItem label="机器人名称" prop="name">
                <Input v-model="editForm.name" placeholder=""/>
            </FormItem>
            <FormItem label="WebHook" prop="webhook">
                <Input v-model="editForm.webhook" placeholder=""/>
                <Poptip placement="right-start" trigger="hover">
                    如何得到WebHook？
                    <div class="api" slot="content">
                        <video src="~@/video/bot-help.mp4" width="500px" loop muted controls autoplay>
                            您的浏览器不支持 video 标签。
                        </video>
                    </div>
                </Poptip>
            </FormItem>
            <FormItem label="创建Android版本时提醒" prop="ANDROID_VERSION_CREATED">
                <Poptip trigger="hover" placement="right-start" :transfer=isTransfer>
                    <i-switch size="large" v-model="editForm.ANDROID_VERSION_CREATED">
                        <span slot="open">开启</span>
                        <span slot="close">关闭</span>
                    </i-switch>
                    <div class="api" slot="content">
                        <img src="~@/images/manual/version-new-tip.png" width="320px"/>
                    </div>
                </Poptip>
            </FormItem>
            <FormItem label="创建iOS版本时提醒" prop="IOS_VERSION_CREATED">
                <Poptip trigger="hover"  placement="right" :transfer=isTransfer>
                    <i-switch size="large" v-model="editForm.IOS_VERSION_CREATED">
                        <span slot="open">开启</span>
                        <span slot="close">关闭</span>
                    </i-switch>
                    <div class="api" slot="content">
                        <img src="~@/images/manual/version-new-tip-ios.png" width="320px"/>
                    </div>
                </Poptip>
            </FormItem>
            <FormItem label="创建自定义接口时提醒" prop="CUSTOM_API_CREATED">
                <Poptip trigger="hover"  placement="right-end" :transfer=isTransfer>
                    <i-switch size="large" v-model="editForm.CUSTOM_API_CREATED">
                        <span slot="open">开启</span>
                        <span slot="close">关闭</span>
                    </i-switch>
                    <div class="api" slot="content">
                        <img src="~@/images/manual/custom-api-new-tip.png" width="320px"/>
                    </div>
                </Poptip>
            </FormItem>
        </Form>
        <Spin size="large" fix v-if="inLoading">
            <Icon type="load-c" size=18 class="spin-icon-load" style="margin-bottom: 12px"></Icon>
            <div>正在加载...</div>
        </Spin>
        <Row class="footer">
            <Col span="4" class="footer-left">
                <Poptip
                        confirm
                        title="确定要删除吗?"
                        @on-ok="handleDelete">
                    <Button type="error" size="large">删除绑定</Button>
                </Poptip>
            </Col>
            <Col span="20">
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
            isTransfer: true,
            inLoading: false,
            isEdit: false, // 是否为编辑状态，false 表示创建，true 编辑中
            editFormRule: {
                name: [
                    { required: true, message: '请输入机器人名称', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ],
                webhook: [
                    { required: true, message: '请输入WebHook', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ]
            },
            editForm: {
                name: '',
                webhook: '',
                ANDROID_VERSION_CREATED: false,
                IOS_VERSION_CREATED: false,
                CUSTOM_API_CREATED: false
            },
            disabledAppId: ''
        };
    },
    created () {
        this.getBot();
    },
    methods: {
        async getBot () {
            let response = await http.get(`/chatbot/getByAppId/${this.appId}`);
            if (response.data.code === 20039) {
                // 没有创建，变成创建模式
                this.isEdit = false;
            } else if (response.data.code === 200) {
                // 变成编辑模式
                this.isEdit = true;
                let data = response.data.data;
                this.disabledAppId = data.id;
                this.editForm = {
                    name: data.name,
                    webhook: data.webhook
                };
                let eventStr = data.activeEvent;
                let eventArray = eventStr.split(',');
                eventArray.forEach(value => {
                    this.editForm[value] = true;
                });
            } else {
                this.$Notice.error({
                    title: '错误',
                    desc: response.message
                });
                this.$emit('close-edit-modal');
            }
        },
        async postBot () {
            let eventArray = [];
            if (this.editForm.ANDROID_VERSION_CREATED) {
                eventArray.push('ANDROID_VERSION_CREATED');
            }
            if (this.editForm.IOS_VERSION_CREATED) {
                eventArray.push('IOS_VERSION_CREATED');
            }
            if (this.editForm.CUSTOM_API_CREATED) {
                eventArray.push('CUSTOM_API_CREATED');
            }
            let query = {
                name: this.editForm.name,
                webhook: this.editForm.webhook,
                events: eventArray,
                appId: this.appId
            };
            let response = await http.post('/chatbot', query);
            return response.data;
        },
        async putBot () {
            let eventArray = [];
            if (this.editForm.ANDROID_VERSION_CREATED) {
                eventArray.push('ANDROID_VERSION_CREATED');
            }
            if (this.editForm.IOS_VERSION_CREATED) {
                eventArray.push('IOS_VERSION_CREATED');
            }
            if (this.editForm.CUSTOM_API_CREATED) {
                eventArray.push('CUSTOM_API_CREATED');
            }
            let query = {
                name: this.editForm.name,
                webhook: this.editForm.webhook,
                events: eventArray,
                appId: this.appId
            };
            let response = await http.put('/chatbot', query);
            return response.data;
        },
        async deleteBot () {
            let response = await http.delete('/chatbot/' + this.appId);
            return response.data;
        },
        handleReset (name) {
            this.$refs[name].resetFields();
            this.$emit('close-edit-modal');
        },
        handleDelete () {
            if (this.isEdit === true) {
                const message = response => {
                    if (response.code === 200) {
                        this.$Notice.success({
                            title: '请求成功',
                            desc: `删除机器人 ${response.data.name} 成功`
                        });
                        this.$emit('close-edit-modal');
                    } else {
                        this.$Notice.error({
                            title: '请求失败',
                            desc: response.message
                        });
                    }
                };

                this.deleteBot().then(message);
            }
            this.$refs['editFormRule'].resetFields();
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
                            desc: `${eventText}机器人 ${response.data.name} 成功`
                        });

                        this.$emit('close-edit-modal');
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
        .footer-left{
            text-align: left;
        }
    }
</style>
