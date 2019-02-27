<template>
    <div id="admin-bind-app">
        <Form :model="queryParams" :label-width="80">
            <Row>
                <Col span="12">
                    <FormItem label="应用名称">
                        <Input v-model="queryParams.appName"/>
                    </FormItem>
                </Col>
                <Col span="12">
                    <Button type="primary" icon="md-search" style="margin: 0 14px" @click="searchApps">搜索</Button>
                </Col>
            </Row>
        </Form>
        <Table border :columns="columns" :data="tableList"/>
        <div style="margin: 10px 0; text-align: right">
            <Page show-total show-elevator show-sizer :total="total" :page-size="pageSize"
                  :current.sync="currentPage" @on-page-size-change="changePageSize"/>
        </div>
        <Spin size="large" fix v-if="inLoading">
            <Icon type="load-c" size=18 class="spin-icon-load" style="margin-bottom: 12px"></Icon>
            <div>正在加载...</div>
        </Spin>
        <Row class="footer">
            <Col span="24">
                <Button type="primary" size="large" @click="handleClose()">关闭</Button>
            </Col>
        </Row>
    </div>
</template>
<script>
import { http } from '@/libs/util';
export default {
    props: {
        userId: {
            type: String
        }
    },
    data () {
        return {
            inLoading: true,
            // 分页
            currentPage: 1,
            total: 0,
            pageSize: 10,
            queryParams: {
                appName: ''
            },
            columns: [
                {
                    title: '#',
                    width: 80,
                    key: 'id'
                },
                {
                    title: '应用名称(租户AppId)',
                    render (h, params) {
                        return h('div', [
                            h('span', params.row.appName),
                            h('div', {
                                style: {
                                    color: '#888'
                                }
                            }, params.row.tenantAppId)
                        ]);
                    }
                },
                {
                    title: '是否授权应用',
                    width: 120,
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h('i-switch', {
                                props: {
                                    value: this.userApps.indexOf(params.row.id) >= 0
                                },
                                on: {
                                    'on-change': (value) => {
                                        let status = this.userApps.indexOf(params.row.id) >= 0 ? 'unBind' : 'bind';
                                        this.putChangeStatus(params.row.id, status);
                                    }
                                }
                            })
                        ]);
                    }
                }
            ],
            tableList: [],
            userApps: [] // 用户已经绑定的 APP ID
        };
    },
    created () {
        this.getApps();
    },
    methods: {
        async getApps () {
            this.inLoading = true;
            let response = await http.get('/admin/app/list', {
                params: {
                    page: this.currentPage,
                    pageSize: this.pageSize,
                    appName: this.queryParams.appName
                }
            });

            let userApps = await this.getUserApps();
            if (userApps.code !== 200) {
                this.$Notice.error({
                    title: '请求失败',
                    desc: response.data.message
                });
                this.handleClose();
                return false;
            }
            this.userApps = [];
            userApps.data.forEach(app => {
                this.userApps.push(app.appId);
            });

            if (response.data.code === 200) {
                this.tableList = response.data.data.records;
                this.total = response.data.data.total;
                this.currentPage = response.data.data.current;
            } else {
                this.$Notice.error({
                    title: '请求失败',
                    desc: response.data.message
                });
            }

            this.inLoading = false;
        },
        async getUserApps () {
            let response = await http.get('/admin/app/list/only/bind', {
                params: {
                    userId: this.userId
                }
            });

            return response.data;
        },
        async putChangeStatus (appId, status) {
            let response = await http.put(`/admin/${this.userId}/${appId}/${status}`);
            let statusText = status === 'bind' ? '绑定' : '取消绑定';

            if (response.data.code === 200) {
                this.$Notice.success({
                    title: '请求成功',
                    desc: `${statusText}成功`
                });
            } else {
                this.$Notice.error({
                    title: '请求失败',
                    desc: response.data.message
                });
            }

            this.getApps();
        },
        searchApps () {
            this.currentPage = 1;
            this.getApps();
        },
        handleClose () {
            this.$emit('close-edit-modal');
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getApps();
        }
    },
    watch: {
        'currentPage': 'getApps'
    }
};
</script>
<style scoped lang="scss">
    #admin-bind-app{
        .footer {
            text-align: right;
            border-top: 1px solid #e9eaec;
            padding: 14px 0 0 0;
            line-height: 1;
        }
    }
</style>
