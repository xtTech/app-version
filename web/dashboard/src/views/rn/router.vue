<template>
    <div id="rn-router">
        <Card>
            <div class="form">
                <Form :model="queryParams" :label-width="140">
                    <Row>
                        <Col span="6">
                            <FormItem label="通用昵称">
                                <Input v-model="queryParams.routeName" placeholder=""/>
                            </FormItem>
                        </Col>
                        <Col span="6">
                            <FormItem label="被拦截 URL">
                                <Input v-model="queryParams.routeKey" placeholder=""/>
                            </FormItem>
                        </Col>
                        <Col span="6">
                            <FormItem label="目标 URL">
                                <Input v-model="queryParams.routeValue" placeholder=""/>
                            </FormItem>
                        </Col>
                        <Col span="6">
                            <FormItem label="状态">
                                <Select v-model="queryParams.routeStatus" filterable clearable>
                                    <Option v-for="item in routerStatus" :value="item.value" :key="item.value" :label="item.label" />
                                </Select>
                            </FormItem>
                        </Col>
                    </Row>
                    <Row>
                        <Col span="24" style="text-align: right">
                            <Button type="default" icon="md-add" @click="createRoute" style="margin-right: 14px">添加 RN 路由</Button>
                            <Button type="primary" icon="md-search" @click="searchRouter">搜索路由</Button>
                        </Col>
                    </Row>
                </Form>
            </div>
            <Table border :columns="columns" :data="tableList" />
            <Row class="footer">
                <Col type="flex" justify="end">
                    <Page show-total show-elevator show-sizer :total="total" :page-size="pageSize"
                          :current.sync="currentPage" @on-page-size-change="changePageSize"/>
                </Col>
            </Row>
        </Card>
        <Modal v-model="inEdit" :mask-closable="false" :title="rnEditTitle" class="rn-router-modal">
            <rn-edit v-if="inEdit" :rnId="rnEditID" @close-edit-modal="handelCloseEdit"></rn-edit>
            <div slot="footer"></div>
        </Modal>
        <Spin size="large" fix v-if="inSearch"></Spin>
    </div>
</template>
<script>
import { http, formatDate } from '@/libs/util';
import rnEdit from './editRoute';

export default {
    data () {
        return {
            total: 0,
            pageSize: 10,
            currentPage: 1,
            queryParams: {
                routeName: '',
                routeKey: '',
                routeStatus: '',
                routeValue: ''
            },
            routerStatus: [
                {
                    value: 0,
                    label: '关闭'
                },
                {
                    value: 1,
                    label: '线上开启'
                },
                {
                    value: 2,
                    label: '测试需要'
                }
            ],
            columns: [
                {
                    title: '#',
                    key: 'id',
                    fixed: 'left',
                    width: 80
                },
                {
                    title: '通用昵称',
                    key: 'routeName',
                    width: 170,
                    fixed: 'left'
                },
                {
                    title: '被拦截 URL',
                    minWidth: 210,
                    render: (h, params) => {
                        return h('div', {
                            style: `display: -webkit-box;
                                -webkit-box-orient: vertical;
                                -webkit-line-clamp: 2;
                                height: 24px;
                                overflow: hidden;
                                line-height: 1;
                                font-size: 12px;`,
                            on: {
                                click: () => {
                                    this.$Modal.confirm({
                                        cancelText: ' ',
                                        render: (h) => {
                                            return h('p', {
                                                style: 'word-wrap: break-word;'
                                            }, params.row.routeKey);
                                        }
                                    });
                                }
                            }
                        }, params.row.routeKey);
                    }
                },
                {
                    title: '目标 URL',
                    minWidth: 200,
                    key: 'routeValue',
                    render: (h, params) => {
                        return h('div', {
                            style: `display: -webkit-box;
                                -webkit-box-orient: vertical;
                                -webkit-line-clamp: 2;
                                height: 24px;
                                overflow: hidden;
                                line-height: 1;
                                font-size: 12px;`,
                            on: {
                                click: () => {
                                    this.$Modal.confirm({
                                        cancelText: ' ',
                                        render: (h) => {
                                            return h('p', {
                                                style: 'word-wrap: break-word;'
                                            }, params.row.routeValue);
                                        }
                                    });
                                }
                            }
                        }, params.row.routeValue);
                    }
                },
                {
                    title: 'iOS 版本区间',
                    minWidth: 180,
                    render: (h, params) => {
                        return h('span', this.versionFilter(params.row.iosEnabled, params.row.iosMin, params.row.iosMax));
                    }
                },
                {
                    title: 'Android 版本区间',
                    minWidth: 180,
                    render: (h, params) => {
                        return h('span', this.versionFilter(params.row.androidEnabled, params.row.androidMin, params.row.androidMax));
                    }
                },
                {
                    title: '最后保存时间',
                    minWidth: 180,
                    render: (h, params) => {
                        return h('span', formatDate(new Date(params.row.updatedTime), 'yyyy-MM-dd hh:mm:ss'));
                    }
                },
                {
                    title: '状态',
                    minWidth: 90,
                    render: (h, params) => {
                        return h('span', this.routerStatusFilter(params.row.routeStatus));
                    }
                },
                {
                    title: '操作',
                    width: 170,
                    fixed: 'right',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    type: 'info',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: (value) => {
                                        this.editRoute(params.row.id);
                                    }
                                }
                            }, '编辑'),
                            h('Poptip', {
                                props: {
                                    confirm: true,
                                    transfer: true,
                                    width: 260,
                                    placement: 'top-end',
                                    title: '确定删除 [' + params.row.routeName + '] RN 路由吗？'
                                },
                                on: {
                                    'on-ok': () => {
                                        this.delRoute(params.row.id);
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
            inSearch: false,
            inEdit: false,
            rnEditID: 0,
            rnEditTitle: ''
        };
    },
    components: { rnEdit },
    created () {
        this.getRoutes();
    },
    filters: {},
    methods: {
        async getRoutes () {
            this.inSearch = true;

            let response = await http.get('/route', {
                params: {
                    page: this.currentPage,
                    pageSize: this.pageSize,
                    routeName: this.queryParams.routeName,
                    routeKey: this.queryParams.routeKey,
                    routeValue: this.queryParams.routeValue,
                    routeStatus: this.queryParams.routeStatus
                }
            });

            if (response.data.code === 200) {
                this.tableList = response.data.data.records;
                this.total = response.data.data.total;
            } else {
                this.total = 0;
                this.tableList = [];
            }

            this.inSearch = false;
        },
        async delRoute (id) {
            let response = await http.delete('/route/' + id);

            if (response.data.code !== 200) {
                this.$Notice.error({
                    title: '错误',
                    desc: response.data.message
                });
            } else {
                this.$Message.success('删除成功!');
            }
            this.getRoutes();
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getRoutes();
        },
        createRoute () {
            this.rnEditTitle = '添加 RN 路由';
            this.rnEditID = 0;
            this.inEdit = true;
        },
        editRoute (id) {
            this.rnEditTitle = '编辑 RN 路由';
            this.rnEditID = id;
            this.inEdit = true;
        },
        searchRouter () {
            this.currentPage = 1;
            this.getRoutes();
        },
        /**
         * 关闭编辑窗口触发事件
        */
        handelCloseEdit () {
            this.inEdit = false;
            this.getRoutes();
        },
        routerStatusFilter (routeStatus) {
            // 路由状态 0:关闭 1:线上开启 2:测试需要'
            let statusText = '';
            switch (routeStatus) {
                case 0:
                    statusText = '关闭';
                    break;
                case 1:
                    statusText = '线上开启';
                    break;
                case 2:
                    statusText = '测试需要';
                    break;
                default:
                    statusText = '未知';
            }

            return statusText;
        },
        versionFilter (status, min = null, max = null) {
            let statusText = '关闭';
            if (status === 1) {
                if (min !== '' && max !== '') { statusText = min + ' ~ ' + max; } else if (min !== '') { statusText = '>= ' + min; } else if (max !== '') { statusText = '<= ' + max; }
            }
            return statusText;
        }
    },
    watch: {
        '$route' (route) {
            this.title = route.meta.title;
        },
        'currentPage': 'getRoutes'
    }
};
</script>
<style lang="scss">
    .rn-router-modal {
        .ivu-modal {
            width: 80% !important;
            max-width: 900px;
        }
        .ivu-modal-footer {
            display: none;
        }
    }
</style>
<style scoped lang="scss">
    #rn-router {
        position: relative;
        .form {
            margin-bottom: 20px;
        }
        .footer {
            text-align: right;
            margin-top: 20px;
        }
    }
</style>
