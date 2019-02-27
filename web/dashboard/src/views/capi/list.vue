<template>
    <div id="capi-list">
        <Card>
            <div class="form">
                <Form :model="queryParams" :label-width="80">
                    <Row>
                        <Col span="6">
                            <FormItem label="接口名称">
                                <Input v-model="queryParams.customName" placeholder=""/>
                            </FormItem>
                        </Col>
                        <Col span="6">
                            <FormItem label="设备系统">
                                <Select v-model="queryParams.osType" filterable clearable>
                                    <Option v-for="item in osTypes" :value="item.value" :key="item.value" :label="item.label" />
                                </Select>
                            </FormItem>
                        </Col>
                        <Col span="12" style="margin-top: 1px;">
                            <Button type="primary" icon="md-search" @click="searchCApi" style="margin: 0 14px">搜索</Button>
                            <Button type="default" icon="md-add" @click="createCApi">添加自定义接口</Button>
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
        <Modal v-model="inEdit" :mask-closable="false" :title="cApiEditTitle" class="capi-list-modal">
            <c-api-edit v-if="inEdit" :c-api-id="cApiEditID" @close-edit-modal="handelCloseEdit"></c-api-edit>
            <div slot="footer"></div>
        </Modal>
        <Modal v-model="inViewCode" :mask-closable="false" :width="800" cancel-text=" " :closable="true" title="配置内容" class="viewcode">
            <div v-if="inViewCode">
                <div ref="code"></div>
            </div>
        </Modal>
        <Spin size="large" fix v-if="inSearch"></Spin>
    </div>
</template>
<script>
import { http, formatDate } from '@/libs/util';

import jsbeautifier from 'js-beautify';
import cApiEdit from './edit';
import CodeMirror from 'codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/neo.css';
import 'codemirror/addon/fold/foldgutter.css';
import 'codemirror/addon/fold/foldcode';
import 'codemirror/addon/fold/foldgutter';
import 'codemirror/addon/fold/brace-fold';
import 'codemirror/addon/fold/comment-fold';
import 'codemirror/mode/javascript/javascript';
export default {
    data () {
        return {
            total: 0,
            pageSize: 10,
            currentPage: 1,
            queryParams: {
                osType: '',
                customName: ''
            },
            osTypes: [
                {
                    value: 'ios',
                    label: 'iOS'
                },
                {
                    value: 'android',
                    label: 'Android'
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
                    title: '接口名称',
                    minWidth: 140,
                    fixed: 'left',
                    key: 'customName'
                },
                {
                    title: '接口 Key',
                    minWidth: 140,
                    key: 'customKey'
                },
                {
                    title: '配置内容',
                    width: 90,
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    type: 'info',
                                    size: 'small'
                                },
                                on: {
                                    click: () => {
                                        this.viewCode = params.row.customContent;
                                        this.inViewCode = true;
                                    }
                                }
                            }, '查看')
                        ]);
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
                        return h('span', this.customStatusFilter(params.row.customStatus));
                    }
                },
                {
                    title: '操作',
                    width: 150,
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
                                        this.editCApi(params.row.id);
                                    }
                                }
                            }, '编辑'),
                            h('Poptip', {
                                props: {
                                    confirm: true,
                                    transfer: true,
                                    width: 260,
                                    placement: 'top-end',
                                    title: '确定删除 [' + params.row.customName + '] 自定义接口吗？'
                                },
                                on: {
                                    'on-ok': () => {
                                        this.delCApi(params.row.id);
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
            cApiEditID: 0,
            cApiEditTitle: '',
            inViewCode: false,
            viewCode: ''
        };
    },
    components: { cApiEdit },
    created () {
        this.getCapis();
    },
    methods: {
        async getCapis () {
            this.inSearch = true;

            let response = await http.get('/capi', {
                params: {
                    page: this.currentPage,
                    pageSize: this.pageSize,
                    osType: this.queryParams.osType,
                    customName: this.queryParams.customName
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
        async delCApi (id) {
            let response = await http.put('/capi/' + id);

            if (response.data.code !== 200) {
                this.$Notice.error({
                    title: '错误',
                    desc: response.data.message
                });
            } else {
                this.$Message.success('删除成功!');
            }
            this.getCapis();
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getCapis();
        },
        createCApi () {
            this.cApiEditTitle = '添加自定义接口';
            this.cApiEditID = 0;
            this.inEdit = true;
        },
        editCApi (id) {
            this.cApiEditTitle = '编辑 自定义 接口';
            this.cApiEditID = id;
            this.inEdit = true;
        },
        searchCApi () {
            this.currentPage = 1;
            this.getCapis();
        },
        /**
        * 关闭编辑窗口触发事件
        */
        handelCloseEdit () {
            this.inEdit = false;
            this.getCapis();
        },
        customStatusFilter (customStatus) {
            // 路由状态 0:关闭 1:线上开启 2:测试需要'
            let statusText = '';
            switch (customStatus) {
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
        'inViewCode' (status) {
            if (status === true) {
                this.$nextTick(() => {
                    new CodeMirror(this.$refs.code, {
                        value: jsbeautifier(this.viewCode),
                        lineNumbers: true,
                        lineWrapping: true,
                        foldGutter: true,
                        readOnly: true,
                        autofocus: true,
                        cursorBlinkRate: -1,
                        cursorScrollMargin: 10,
                        theme: 'neo',
                        gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter'],
                        mode: 'application/ld+json'
                    });
                });
            } else {
                this.viewCode = '';
            }
        },
        'currentPage': 'getCapis'
    }
};
</script>
<style lang="scss">
    .capi-list-modal{
        .ivu-modal {
            width: 80% !important;
            max-width: 900px;
        }
        .ivu-modal-footer {
            display: none;
        }
    }
    .viewcode{
        .ivu-modal-body{
            padding: 0;
        }
    }
</style>
<style scoped lang="scss">
    #capi-list {
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
