<template>
    <div id="page-login" style="background:#eee;">
        <Row type="flex" justify="center" align="middle" style="height: 70%">
            <Col span="6">
                <Card :bordered="false" :dis-hover="true" style="padding: 10px 20px;text-align: center">
                    <p style="width: 100%; margin-top: 5px;margin-bottom: 20px">注册APP版本管理系统</p>
                    <Form :model="formItem" ref="formItem" :rules="formRule">
                        <FormItem prop="phone">
                            <Input type="text" v-model="formItem.phone" placeholder="请输入手机号" :disabled="loading" @on-enter="handleSubmit">
                                <Icon type="ios-phone-portrait" slot="prepend"></Icon>
                            </Input>
                        </FormItem>
                        <FormItem prop="password">
                            <Input type="password" v-model="formItem.password" placeholder="请输入密码" :disabled="loading" @on-enter="handleSubmit">
                                <Icon type="ios-lock-outline" slot="prepend"></Icon>
                            </Input>
                        </FormItem>
                        <FormItem prop="passwordConfirm">
                            <Input type="password" v-model="formItem.passwordConfirm" placeholder="请确认密码" :disabled="loading" @on-enter="handleSubmit">
                                <Icon type="ios-unlock-outline" slot="prepend"></Icon>
                            </Input>
                        </FormItem>
                        <FormItem style="margin-bottom: 10px">
                            <Button type="success" @click="handleSubmit()" long :loading="loading">注册</Button>
                        </FormItem>
                        <FormItem style="margin-bottom: 0">
                            <Button type="primary" @click="goToLogin()" long>回到登录页面</Button>
                        </FormItem>
                    </Form>
                </Card>
            </Col>
        </Row>

    </div>
</template>
<script>
import { setAdmin, setLogin } from '@/libs/account';
import { http } from '@/libs/util';
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
            loading: false,
            formItem: {
                phone: '',
                password: '',
                passwordConfirm: ''
            },
            formRule: {
                phone: [
                    { required: true, message: '请输入正确的手机号', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' },
                    { required: true, type: 'string', min: 11, max:11, message: '请输入正确的手机号', trigger: 'blur'  },
                    { required: true, pattern: /^[0-9]*$/g, message: '请输入正确的手机号', trigger: 'blur' }
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
    methods: {
        handleSubmit () {
            this.$refs['formItem'].validate(async valid => {
                if (!valid) {
                    this.$Message.error('请先完成所有设置!');
                    return false;
                }
                this.loading = true;
                let that = this;
                http.post('/user/register',{
                    phone: this.formItem.phone,
                    password: md5(this.formItem.password),
                    passwordConfirm: md5(this.formItem.passwordConfirm)
                })
                .then(resp => {
                    if(resp.data.code!=200){
                        this.$Notice.error({
                            title: '注册失败',
                            desc: resp.data.message
                        });
                    }else{
                        this.$Notice.success({
                            title: '注册成功'
                        });
                        this.$router.push({
                            name: 'login'
                        });
                    }
                    that.loading =false;
                })
                .catch(function (error) {
                    this.$Notice.error({
                        title: '注册失败',
                        desc: error
                    });
                    that.loading =false;
                });
            });

        },
        goToLogin() {
            this.$router.push({
                name: 'login'
            });
        }
    },
    created: function () {
    }
};
</script>
<style scoped>
    #page-login{
        height: 100%;
        overflow: hidden;
    }
</style>
