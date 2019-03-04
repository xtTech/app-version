<template>
    <div id="home">
        <Card style="min-height: 400px;">
            <div class="logo">
                <img src="@/images/logo.png">
                <h2 style="margin-bottom: 50px">
                    <p>APP版本管理系统</p>
                </h2>
            </div>
            <div class="content" style="width: 400px;margin: 0 auto;">
                <Row :gutter="5">
                    <Col span="19">
                        <Select class="select" v-model="appId" @on-change="handelChangeApp" placeholder="请先搜索或选择应用" filterable>
                            <Option v-for="app in appList" :value="app.appId" :key="app.appId" :label="app.appName">
                                <span>{{ app.appName }}</span>
                                <span style="float:right;color:#ccc;max-width:100px;overflow: hidden">{{app.tenantAppId}}</span>
                            </Option>
                        </Select>
                    </Col>
                    <Col span="5">
                        <Button type="primary" icon="ios-add" @click="inAdd = !inAdd">创建</Button>
                    </Col>
                    <Col span="24">
                        <div class="nameplate" v-if="app">
                            <span>{{app.appId}}</span>
                            <h3>{{app.appName}}</h3>
                            <p>{{app.tenantAppId}}</p>
                        </div>
                    </Col>
                </Row>
            </div>
        </Card>

        <Modal v-model="inAdd" :mask-closable="false" title="创建一个APP" class="admin-app-modal">
            <app-edit v-if="inAdd" :admin="false" @close-edit-modal="handelCloseEdit"></app-edit>
            <div slot="footer"></div>
        </Modal>
    </div>
</template>
<script>
import { isLogin, getApp, getAppId, getApps, switchApp } from '@/libs/account';
import appEdit from '../admin/editApp';

export default {
    components: { appEdit },
    data () {
        return {
            isLogin: isLogin(),
            app: getApp(),
            appId: getAppId() ? getAppId() : '',
            appList: [],
            inAdd: false
        };
    },
    async created () {
        if (this.isLogin === false) {
            this.$router.push({
                name: 'login'
            });
            return false;
        }

        let apps = await getApps();
        if (apps) { this.appList = apps; }

        console.log(this.apps);
    },
    mounted () {

    },
    methods: {
        handelChangeApp (appId) {
            if (appId === getAppId()) return false;
            let app = this.appList.find(e => {
                return e.appId === appId;
            });

            switchApp(app);
        },
        async handelCloseEdit () {
            this.inAdd = false;
            let apps = await getApps();
            if (apps) { this.appList = apps; }
        }
    }
};
</script>
<style lang="scss">
    .admin-app-modal{
        .ivu-modal-footer {
            display: none!important;
        }
    }
    #home {
        .admin-app-modal{
            .ivu-modal {
                width: 60% !important;
                max-width: 600px;
            }
            .ivu-modal-footer {
                display: none;
            }
        }
        .logo {
            text-align: center;
            img {
                display: inline-block;
                height: 120px;
                width: auto;
            }
        }
        .nameplate{
            width: 400px;
            position: relative;
            margin: 36px auto;
            border-radius: 4px;
            border: 1px solid #eee;
            padding: 12px;
            border-top: 2px solid #2d8cf0;
            overflow: hidden;
            h3{
                font-size: 20px;
                margin: 12px 0;
            }
            p{
                color: #777777;
                margin: 6px 0;
                font-size: 12px;
            }
            h3,p{
                z-index: 1;
                position: relative;
                word-break: break-all;
            }
            span{
                position: absolute;
                font-size: 80px;
                font-weight: bold;
                color: rgba(249, 249, 249, 0.78);
                right: 12px;
                line-height: .7;
                z-index: 0;
                user-select: none;
            }
        }
    }
</style>
