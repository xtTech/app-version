<template>
    <div id="page-login" style="background:#eee;">

        <Row type="flex" justify="center" align="middle" style="height: 80%">
            <Col span="6">
                <Card :bordered="false" :dis-hover="true" style="padding: 10px 20px;padding-top:0;text-align: center">
                    <img src="@/images/logo.png" width="50%">
                    <p style="width: 100%; margin-top: 5px;margin-bottom: 20px">登录APP版本管理系统</p>
                    <Form ref="formInline">
                        <FormItem prop="phone">
                            <Input type="text" v-model="phone" placeholder="请输入手机号" :disabled="loading"  @on-enter="handleSubmit">
                                <Icon type="ios-phone-portrait" slot="prepend"></Icon>
                            </Input>
                        </FormItem>
                        <FormItem prop="password">
                            <Input type="password" v-model="password" placeholder="请输入密码" :disabled="loading" @on-enter="handleSubmit">
                                <Icon type="ios-lock-outline" slot="prepend"></Icon>
                            </Input>
                        </FormItem>
                        <FormItem style="margin-bottom: 10px">
                            <Button type="primary" @click="handleSubmit()" long :loading="loading">登录</Button>
                        </FormItem>
                        <FormItem style="margin-bottom: 0">
                            <Button type="success" @click="goToRegister()" long>注册</Button>
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
        return {
            loading: false,
            phone: null,
            password: null
        };
    },
    methods: {
        handleSubmit () {
            this.loading = true;
            if(!this.phone || !this.password){
                this.$Message.warning('请将必填项填写完整');
                this.loading = false;
                return;
            }
            this.phone = this.phone.trim()
            this.password = this.password.trim()
            if(this.phone.length !== 11 || isNaN(Number(this.phone))){
                this.$Message.warning('请输入正确的手机号');
                this.loading = false;
                return;
            }
            if(this.password.length < 6 || this.password.length > 32){
                this.$Message.warning('密码长度为6-32');
                this.loading = false;
                return;
            }
            let that = this;
            http.post('/user/login',{
                phone: this.phone,
                password: md5(this.password),
            })
                .then(resp => {
                    if(resp.data.code!=200){
                        this.$Notice.error({
                            title: '登录失败',
                            desc: resp.data.message
                        });
                    }else{
                        this.$Notice.success({
                            title: '登录成功'
                        });
                        setLogin(resp.data.data);
                        setAdmin(resp.data.data.isAdmin === 1);
                        this.$router.push({
                            name: 'switch-app'
                        });
                    }
                    that.loading =false;
                })
                .catch(function (error) {
                    this.$Notice.error({
                        title: '登录失败',
                        desc: error
                    });
                    that.loading =false;
                });
        },
        goToRegister() {
            this.$router.push({
                name: 'register'
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
