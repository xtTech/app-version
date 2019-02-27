<template>
    <div id="admin-app">
        <Card>
            <Form :model="queryParams" :label-width="80">
                <Row>
                    <Col span="6">
                        <FormItem label="应用名称">
                            <Input v-model="queryParams.appName"/>
                        </FormItem>
                    </Col>
                    <Col span="18">
                        <Button type="primary" icon="md-search" @click="searchApps" style="margin: 0 14px">搜索应用</Button>
                        <Button type="default" icon="md-add" @click="createApp">添加应用</Button>
                    </Col>
                </Row>
            </Form>
            <Table border :columns="columns" :data="tableList"/>
            <div style="margin: 10px 0 0 0; text-align: right">
                <Page show-total show-elevator show-sizer :total="total" :page-size="pageSize"
                      :current.sync="currentPage" @on-page-size-change="changePageSize"/>
            </div>
        </Card>
        <Modal v-model="inEdit" :mask-closable="false" :title="appEditTitle" class="admin-app-modal">
            <app-edit v-if="inEdit" :app-id="appEditID" @close-edit-modal="handelCloseEdit"></app-edit>
            <div slot="footer"></div>
        </Modal>
        <Modal v-model="inBotEdit" :mask-closable="false" :title="appBotEditTitle" class="admin-app-bot-modal">
            <app-bot-edit v-if="inBotEdit" :app-id="appBotEditID" :admin="true" @close-edit-modal="handelCloseBotEdit"></app-bot-edit>
            <div slot="footer"></div>
        </Modal>
        <Spin size="large" fix v-if="inLoading"></Spin>
    </div>
</template>
<script>
import { http, formatDate } from '@/libs/util';
import appEdit from './editApp';
import appBotEdit from './editBot';
export default {
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
            // table
            columns: [
                {
                    title: '#',
                    key: 'id',
                    fixed: 'left',
                    width: 80
                },
                {
                    title: '应用名称',
                    key: 'appName',
                    fixed: 'left',
                    width: 140
                },
                {
                    title: '租户AppId',
                    minWidth: 140,
                    key: 'tenantAppId'
                },
                {
                    title: '添加时间',
                    minWidth: 160,
                    algin: 'center',
                    render: (h, params) => {
                        let date = new Date(params.row.createdTime);
                        return h('div', formatDate(date, 'yyyy-MM-dd hh:mm:ss'));
                    }
                },
                {
                    title: '添加者',
                    minWidth: 140,
                    key: 'createdBy'
                },
                {
                    title: '操作',
                    minWidth: 200,
                    fixed: 'right',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '12px'
                                },
                                on: {
                                    click: () => {
                                        this.editBot(params.row.id);
                                    }
                                }
                            }, '钉钉群机器人'),
                            h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '12px'
                                },
                                on: {
                                    click: () => {
                                        this.editApp(params.row.id);
                                    }
                                }
                            }, '编辑'),
                            h('Poptip', {
                                props: {
                                    confirm: true,
                                    transfer: true,
                                    width: 260,
                                    placement: 'top-end',
                                    title: '确定删除[' + params.row.appName + ']这个应用吗？'
                                },
                                on: {
                                    'on-ok': (value) => {
                                        this.delApp(params.row.id);
                                    }
                                }
                            }, [
                                h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small'
                                    }
                                }, '删除')
                            ])
                        ]);
                    }
                }
            ],
            tableList: [],
            inEdit: false,
            inBotEdit: false,
            appEditID: 0,
            appEditTitle: '',
            appBotEditID: 0,
            appBotEditTitle: ''
        };
    },
    components: { appEdit, appBotEdit },
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
        async delApp (id) {
            let response = await http.delete('/admin/app/' + id);
            if (response.data.code === 200) {
                this.$Notice.success({
                    title: '请求成功',
                    desc: `删除应用[${response.data.data.appName}]成功`
                });
            } else {
                this.$Notice.error({
                    title: '请求失败',
                    desc: response.data.message
                });
            }

            this.getApps();
        },
        editApp (id) {
            this.appEditTitle = '编辑应用';
            this.appEditID = id;
            this.inEdit = true;
        },
        editBot (id) {
            this.appBotEditTitle = '机器人设置';
            this.appBotEditID = id;
            this.inBotEdit = true;
        },
        createApp () {
            this.appEditTitle = '添加应用';
            this.appEditID = 0;
            this.inEdit = true;
        },
        searchApps () {
            this.currentPage = 1;
            this.getApps();
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getApps();
        },
        handelCloseEdit () {
            this.inEdit = false;
            this.getApps();
        },
        handelCloseBotEdit () {
            this.inBotEdit = false;
            this.getApps();
        }
    },
    watch: {
        'currentPage': 'getApps'
    }
};
</script>
<style lang="scss">
    .admin-app-modal{
        .ivu-modal {
            width: 60% !important;
            max-width: 600px;
        }
        .ivu-modal-footer {
            display: none;
        }
    }
    .admin-app-bot-modal{
        .ivu-modal {
            width: 60% !important;
            max-width: 600px;
        }
        .ivu-modal-footer {
            display: none;
        }
    }
</style>
