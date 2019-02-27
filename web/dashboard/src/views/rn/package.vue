<template>
    <div id="rn-package">
        <Card>
            <div class="form">
                <Form :model="queryParams" :label-width="120">
                    <Row>
                        <Col span="8">
                            <FormItem label="模块名称">
                                <Input v-model="queryParams.rnName" placeholder=""/>
                            </FormItem>
                        </Col>
                        <Col span="8">
                            <FormItem label="通用昵称">
                                <Input v-model="queryParams.rnNickName" placeholder=""/>
                            </FormItem>
                        </Col>
                        <Col span="8">
                            <FormItem label="RN 包状态">
                                <Select v-model="queryParams.rnStatus" filterable clearable>
                                    <Option v-for="item in packageStatus" :value="item.value" :key="item.value" :label="item.label" />
                                </Select>
                            </FormItem>
                        </Col>
                    </Row>
                    <Row>
                        <Col span="24" style="text-align: right">
                            <Button type="default" icon="md-add" @click="createPackage" style="margin-right: 14px">添加 RN 包</Button>
                            <Button type="primary" icon="md-search" @click="searchPackage">搜索 RN 包</Button>
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
        <Modal v-model="inEdit" :mask-closable="false" :title="rnEditTitle" class="rn-package-modal">
            <rn-edit v-if="inEdit" :rnId="rnEditID" @close-edit-modal="handelCloseEdit"></rn-edit>
            <div slot="footer"></div>
        </Modal>
        <Spin size="large" fix v-if="inSearch"></Spin>
    </div>
</template>
<script>
import { http, fmtFileSize, formatDate } from '@/libs/util';
import rnEdit from './editPackage';
export default {
    data () {
        return {
            total: 0,
            pageSize: 10,
            currentPage: 1,
            queryParams: {
                rnName: '',
                rnNickName: '',
                rnStatus: ''
            },
            packageStatus: [
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
                    title: '模块名称',
                    key: 'rnName',
                    fixed: 'left',
                    width: 150
                },
                {
                    title: '包版本号',
                    key: 'rnVersion',
                    width: 140
                },
                {
                    title: '通用昵称',
                    key: 'rnNickName',
                    minWidth: 140
                },
                {
                    title: '客户端类型',
                    minWidth: 140,
                    render: (h, params) => {
                        return h('span', this.packageDeviceFilter(params.row.rnType));
                    }
                },
                {
                    title: '版本区间',
                    minWidth: 180,
                    render: (h, params) => {
                        return h(
                            'div',
                            this.versionFilter(params.row.versionMin, params.row.versionMax)
                        );
                    }
                },
                {
                    title: '资源地址',
                    minWidth: 200,
                    render: (h, params) => {
                        return h(
                            'div',
                            {
                                style: `display: -webkit-box;
                                -webkit-box-orient: vertical;
                                -webkit-line-clamp: 2;
                                height: 24px;
                                overflow: hidden;`,
                                on: {
                                    click: () => {
                                        this.$Modal.confirm({
                                            cancelText: ' ',
                                            render: h => {
                                                return h(
                                                    'p',
                                                    {
                                                        style: 'word-wrap: break-word;'
                                                    },
                                                    params.row.resourceUrl
                                                );
                                            }
                                        });
                                    }
                                }
                            },
                            params.row.resourceUrl
                        );
                    }
                },
                {
                    title: '包大小',
                    minWidth: 140,
                    render: (h, params) => {
                        return h('span', fmtFileSize(params.row.rnSize));
                    }
                },
                {
                    title: '包更新日志',
                    width: 100,
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h(
                                'Button',
                                {
                                    props: {
                                        type: 'info',
                                        size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.$Modal.confirm({
                                                width: '800',
                                                cancelText: ' ',
                                                render: h => {
                                                    return h('div', [
                                                        h(
                                                            'h2',
                                                            {
                                                                style: {
                                                                    fontSize: '20px',
                                                                    marginBottom: '10px',
                                                                    marginTop: '-20px',
                                                                    borderBottom: '1px solid #f7f7f7',
                                                                    paddingBottom: '10px'
                                                                }
                                                            },
                                                            '更新日志'
                                                        ),
                                                        h(
                                                            'pre',
                                                            {
                                                                style: {
                                                                    overflow: 'hidden',
                                                                    overflowX: 'auto'
                                                                }
                                                            },
                                                            params.row.rnUpdateLog
                                                        )
                                                    ]);
                                                }
                                            });
                                        }
                                    }
                                },
                                '查看'
                            )
                        ]);
                    }
                },
                {
                    title: '最后保存时间',
                    width: 180,
                    render: (h, params) => {
                        return h(
                            'span',
                            formatDate(
                                new Date(params.row.updatedTime),
                                'yyyy-MM-dd hh:mm:ss'
                            )
                        );
                    }
                },
                {
                    title: '状态',
                    align: 'center',
                    width: 90,
                    render: (h, params) => {
                        return h('span', this.packageStatusFilter(params.row.rnStatus));
                    }
                },
                {
                    title: '操作',
                    width: 150,
                    fixed: 'right',
                    render: (h, params) => {
                        return h('div', [
                            h(
                                'Button',
                                {
                                    props: {
                                        type: 'info',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: value => {
                                            this.editPackage(params.row.id);
                                        }
                                    }
                                },
                                '编辑'
                            ),
                            h(
                                'Poptip',
                                {
                                    props: {
                                        confirm: true,
                                        width: 260,
                                        transfer: true,
                                        placement: 'top-end',
                                        title: '确定删除 [' + params.row.rnName + '] RN 路由吗？'
                                    },
                                    on: {
                                        'on-ok': () => {
                                            this.delPackage(params.row.id);
                                        }
                                    }
                                },
                                [
                                    h(
                                        'Button',
                                        {
                                            props: {
                                                type: 'error',
                                                size: 'small'
                                            }
                                        },
                                        '删除'
                                    )
                                ]
                            )
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
        this.getPackages();
    },
    methods: {
        async getPackages () {
            this.inSearch = true;

            let response = await http.get('/package', {
                params: {
                    page: this.currentPage,
                    pageSize: this.pageSize,
                    rnName: this.queryParams.rnName,
                    rnNickName: this.queryParams.rnNickName,
                    rnStatus: this.queryParams.rnStatus
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
        async delPackage (id) {
            let response = await http.delete('/package/' + id);
            if (response.data.code !== 200) {
                this.$Notice.error({
                    title: '错误',
                    desc: response.data.message
                });
            } else {
                this.$Message.success('删除成功!');
            }
            this.getPackages();
        },
        searchPackage () {
            this.currentPage = 1;
            this.getPackages();
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getPackages();
        },
        createPackage () {
            this.rnEditTitle = '添加 RN 包';
            this.rnEditID = 0;
            this.inEdit = true;
        },
        editPackage (id) {
            this.rnEditTitle = '编辑 RN 包';
            this.rnEditID = id;
            this.inEdit = true;
        },
        /**
		 * 关闭编辑窗口触发事件
		 */
        handelCloseEdit () {
            this.inEdit = false;
            this.getPackages();
        },
        packageDeviceFilter (type) {
            return type === 1 ? 'Android' : type === 2 ? 'iOS' : '无';
        },
        packageStatusFilter (packageStatus) {
            // 包状态 0:关闭 1:线上开启 2:测试需要'
            let statusText = '';
            switch (packageStatus) {
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
        versionFilter (min = null, max = null) {
            let statusText = '无';
            if (min !== '' && max !== '') {
                statusText = min + ' ~ ' + max;
            } else if (min !== '') {
                statusText = '>= ' + min;
            } else if (max !== '') {
                statusText = '<= ' + max;
            }
            return statusText;
        }
    },
    watch: {
        currentPage: 'getPackages'
    }
};
</script>
<style lang="scss">
.rn-package-modal {
	.ivu-modal-close {
		z-index: 1;
	}
	.ivu-modal {
		width: 80% !important;
		max-width: 900px;
		margin-bottom: 50px;
	}

	.ivu-modal-footer {
		display: none;
	}
}
</style>
<style scoped lang="scss">
#rn-package {
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
