<template>
    <div id="sidebar">
        <Sider :style="{position: 'fixed', height: '100vh', left: 0, overflow: 'auto'}">
            <Menu :active-name="activeNavName" :open-names="openNames" theme="dark" width="200" ref="menus"
                  @on-select="goToPage">
                <MenuItem name="switch-app">
                    <Icon type="md-home" />
                    <span>首页</span>
                </MenuItem>
                <Submenu name="version">
                    <template slot="title">
                        <Icon type="md-git-network" />
                        <span>版本管理</span>
                    </template>
                    <MenuItem name="version-channel">渠道管理</MenuItem>
                    <MenuItem name="version-android">Android版本管理</MenuItem>
                    <MenuItem name="version-ios">iOS版本管理</MenuItem>
                </Submenu>
                <Submenu name="rn">
                    <template slot="title">
                        <Icon type="md-archive" />
                        <span>RN管理</span>
                    </template>
                    <MenuItem name="rn-package">RN包管理</MenuItem>
                    <MenuItem name="rn-router">RN路由管理</MenuItem>
                </Submenu>
                <Submenu name="capi">
                    <template slot="title">
                        <Icon type="md-contract"></Icon>
                        <span>自定义接口</span>
                    </template>
                    <MenuItem name="capi-list">接口设置</MenuItem>
                </Submenu>
                <Submenu name="admin" v-if="admin">
                    <template slot="title">
                        <Icon type="ios-people" />
                        <span>管理员</span>
                    </template>
                    <MenuItem name="admin-app">应用管理</MenuItem>
                    <MenuItem name="admin-user">用户管理</MenuItem>
                    <MenuItem name="admin-log">操作日志</MenuItem>
                </Submenu>
                <Submenu name="manual">
                    <template slot="title">
                        <Icon type="md-document"></Icon>
                        <span>操作手册</span>
                    </template>
                    <MenuItem name="manual-android">Android 版本</MenuItem>
                    <MenuItem name="manual-ios">iOS 版本</MenuItem>
                    <MenuItem name="manual-channel">添加渠道</MenuItem>
                    <MenuItem name="manual-package">添加 RN 包</MenuItem>
                    <MenuItem name="manual-router">添加 RN 路由</MenuItem>
                    <MenuItem name="manual-type">更新类型说明</MenuItem>
                </Submenu>
            </Menu>
        </Sider>
    </div>
</template>
<script>
import { hasObject } from '@/libs/util';
import { isAdmin } from '@/libs/account';
export default {
    data () {
        return {
            activeNavName: '',
            openNames: [],
            admin: isAdmin()
        };
    },
    created () {
        this.routeWatch();
    },
    mounted () {
        this.$nextTick(() => {
            this.routeWatch();
        });
    },
    filters: {},
    methods: {
        routeWatch () {
            this.activeNavName = this.$route.name;
            this.openNames = [this.$route.matched[0].name];
        },
        goToPage (name) {
            this.$router.push({ name });
        }
    },
    watch: {
        '$route': 'routeWatch'
    }
};
</script>
<style lang="scss">
    /*#sidebar {*/
        /*.ivu-layout-sider{*/
            /*width: 50px!important;*/
            /*min-width: 50px!important;*/
            /*max-width: 50px!important;*/
            /*flex: 0 0 50px!important;*/
            /*.ivu-menu-item{*/
                /*.ivu-icon{*/
                    /*margin-right: 0;*/
                    /*font-size: 20px;*/
                    /*& + span{*/
                        /*display: none;*/
                    /*}*/
                /*}*/
            /*}*/
            /*.ivu-menu-submenu-title{*/
                /*& > .ivu-icon{*/
                    /*margin-right: 0;*/
                    /*font-size: 20px;*/
                    /*& + span{*/
                        /*display: none;*/
                        /*& + i{*/
                            /*display: none;*/
                        /*}*/
                    /*}*/
                /*}*/
                /*& + .ivu-menu{*/
                    /*position: absolute;*/
                    /*width: 200px;*/
                    /*z-index: 1;*/
                /*}*/
            /*}*/
            /*.ivu-menu-vertical .ivu-menu-item, .ivu-menu-vertical .ivu-menu-submenu-title{*/
                /*padding: 14px 0px;*/
                /*text-align: center;*/
            /*}*/
        /*}*/
    /*}*/

    #page-admin {
        .page-body{
            // z-index: -1;
            // width: calc(100% - 50px)!important;
        }
    }
</style>
