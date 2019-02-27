<template>
    <div id="version-android-apk">
        <Card>
            <div class="form">
                <Form :model="queryParams" :label-width="80">
                    <Row>
                        <Col span="8">
                            <FormItem label="渠道码">
                                <Input v-model="queryParams.channelCode" placeholder=""/>
                            </FormItem>
                        </Col>
                        <Col span="8">
                            <FormItem label="MD5">
                                <Input v-model="queryParams.md5" placeholder=""/>
                            </FormItem>
                        </Col>
                        <Col span="8">
                            <FormItem label="上架状态">
                                <Select v-model="queryParams.deliveryStatus" filterable clearable>
                                    <Option v-for="item in deliveryStatus" :value="item.value" :key="item.value" :label="item.label" />
                                </Select>
                            </FormItem>
                        </Col>
                    </Row>
                    <Row>
                        <Col span="24" style="text-align: right">
                            <Button type="default" icon="md-add" style="margin-right: 14px" @click="inUploads = !inUploads">添加 Apk 包</Button>
                            <Button type="primary" icon="md-search" @click="searchApk">搜索 Apk 包</Button>
                        </Col>
                    </Row>
                </Form>
            </div>
            <Table border :columns="columns" :data="tableList"/>
            <div style="margin: 10px 0 0 0; text-align: right">
                <Page show-total show-elevator show-sizer :total="total" :page-size="pageSize"
                      :current.sync="currentPage" @on-page-size-change="changePageSize"/>
            </div>
        </Card>
        <Spin size="large" fix v-if="inLoading"></Spin>

        <Modal v-model="inUploads" :mask-closable="false" title="上传 Apk 包" class="version-android-modal">
            <upload-apk v-if="inUploads" :android-id="androidId" :app-name="appName" :app-version="appVersion" @on-upload-success="searchApk"></upload-apk>
            <div slot="footer"></div>
        </Modal>
    </div>
</template>
<script>

import { formatDate, hasObject, http } from '@/libs/util';
import uploadApk from './uploadApk';
const deliveryStatus = [
    {
        label: '未上架',
        value: 0
    },
    {
        label: '已上架',
        value: 1
    }
];
export default {
    data () {
        return {
            androidId: 0,
            appVersion: '',
            appName: '',
            inLoading: true,
            inUploads: false,
            // 分页
            currentPage: 1,
            total: 0,
            pageSize: 10,
            // table
            columns: [
                {
                    title: '#',
                    key: 'id',
                    fixed: 'left',
                    width: 80
                },
                {
                    title: '渠道',
                    key: 'channelCode',
                    minWidth: 140
                },
                {
                    title: '版本号',
                    key: 'version',
                    minWidth: 140
                },
                {
                    title: 'md5',
                    key: 'md5',
                    minWidth: 260
                },
                {
                    title: '下载地址',
                    minWidth: 140,
                    render: (h, params) => {
                        return h('div', {
                            style: `display: -webkit-box;
                                -webkit-box-orient: vertical;
                                -webkit-line-clamp: 2;
                                height: 24px;
                                overflow: hidden;`,
                            on: {
                                click: () => {
                                    this.$Modal.confirm({
                                        cancelText: ' ',
                                        render: (h) => {
                                            return h('p', {
                                            	style: 'word-wrap: break-word;'
                                            }, params.row.ossUrl);
                                        }
                                    });
                                }
                            }
                        }, params.row.ossUrl);
                    }
                },
                {
                    title: '上下架',
                    width: 90,
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h('i-switch', {
                                props: {
                                    value: !(params.row.deliveryStatus === 0 || params.row.deliveryStatus === 2)
                                },
                                on: {
                                    'on-change': (value) => {
                                        let status = params.row.deliveryStatus === 0 || params.row.deliveryStatus === 2 ? 'delivery' : 'undelivery';
                                        this.putChangeStatus(params.row.id, status);
                                    }
                                }
                            })
                        ]);
                    }
                },
                {
                    title: '添加时间',
                    minWidth: 180,
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
                    width: 90,
                    fixed: 'right',
                    render: (h, params) => {
                        return h('div', [
                            h('Poptip', {
                                props: {
                                    confirm: true,
                                    width: 260,
                                    transfer: true,
                                    placement: 'top-end',
                                    title: '确定删除[' + params.row.channelCode + ']这个渠道的版本吗？'
                                },
                                on: {
                                    'on-ok': (value) => {
                                        this.delApk(params.row.id);
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
            // search
            queryParams: {
                channelCode: '',
                md5: '',
                deliveryStatus: null
            },
            deliveryStatus
        };
    },
    components: { uploadApk },
    created () {
        if (hasObject('params.androidId', this.$route) && this.$route.params.androidId > 0) {
            this.androidId = parseInt(this.$route.params.androidId);
            this.getAndroid();
        } else {
            this.$router.replace({
                name: 'version-android'
            });
        }
    },
    methods: {
        async getAndroid () {
            let response = await http.get('/android/' + this.androidId);
            if (response.data.code !== 200) {
                this.$Notice.error({
                    title: '请求失败,请返回',
                    desc: response.data.message
                });
            }

            await this.getApks();

            this.appVersion = response.data.data.appVersion;

            try {
                let app = JSON.parse(localStorage.getItem('app') ? localStorage.getItem('app') : '{}');
                this.appName = app.appName;
            } catch (e) {

            }
        },
        async getApks () {
            this.inLoading = true;
            let response = await http.get('/apk', {
                params: {
                    page: this.currentPage,
                    pageSize: this.pageSize,
                    versionId: this.androidId,
                    channelCode: this.queryParams.channelCode,
                    md5: this.queryParams.md5,
                    deliveryStatus: this.queryParams.deliveryStatus === undefined ? null : this.queryParams.deliveryStatus
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
        async delApk (id) {
            let response = await http.delete('/apk/' + id);
            if (response.data.code === 200) {
                this.$Notice.success({
                    title: '请求成功',
                    desc: `删除渠道[${response.data.data.versionId}]的版本成功`
                });
            } else {
                this.$Notice.error({
                    title: '请求失败',
                    desc: response.data.message
                });
            }

            this.getApks();
        },
        async putChangeStatus (id, status) {
            let response = await http.put('/apk/' + id + '/' + status);
            let statusText = status === 'delivery' ? '上架' : '下架';

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

            this.getApks();
        },
        searchApk () {
            this.currentPage = 1;
            this.getApks();
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getApks();
        }
    },
    beforeRouteLeave (to, from, next) {
        // 导航离开该组件的对应路由时调用
        // 可以访问组件实例 `this`
        this.$Modal.remove();
        next();
    },
    watch: {
        'currentPage': 'getApks'
    }
};
</script>
<style lang="scss">
    .version-android-modal {
        .ivu-modal-close{
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
    #version-android-apk{
        .form{
            margin-bottom: 20px;
        }
    }
</style>
