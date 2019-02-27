<template>
    <div id="version-android">
        <Card>
            <div class="form">
                <Form :model="queryParams" :label-width="80">
                    <Row>
                        <Col span="8">
                            <FormItem label="版本号">
                                <Input v-model="queryParams.appVersion" placeholder=""/>
                            </FormItem>
                        </Col>
                        <Col span="8">
                            <FormItem label="更新类型">
                                <Select v-model="queryParams.updateType" filterable clearable>
                                    <Option v-for="item in updateTypes" :value="item.value" :key="item.value" :label="item.label" />
                                </Select>
                            </FormItem>
                        </Col>
                        <Col span="8">
                            <FormItem label="上架状态">
                                <Select v-model="queryParams.versionStatus" filterable clearable>
                                    <Option v-for="item in versionStatus" :value="item.value" :key="item.value" :label="item.label" />
                                </Select>
                            </FormItem>
                        </Col>
                    </Row>
                    <Row>
                        <Col span="24" style="text-align: right">
                            <Button type="default" icon="md-add" style="margin-right: 14px" @click="toCreatePage">添加 Android 版本</Button>
                            <Button type="primary" icon="md-search" @click="searchAndroids">搜索</Button>
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
        <Modal v-model="inTwoConfirm" title="上架确认" class="user-modal" @on-cancel="twoConfirmCancel">
            <Form :model="twoConfirm" ref="twoConfirm" :label-width="110" :rules="twoConfirmRule">
                <FormItem label="当前上架版本" style="margin-bottom: 0">
                    <p style="user-select: none">{{twoConfirm.appVersion}}</p>
                </FormItem>
                <FormItem label="允许最低版本" style="margin-bottom: 0">
                    <p style="user-select: none">{{twoConfirm.allowLowestVersion}}</p>
                </FormItem>
                <FormItem label="更新类型" style="margin-bottom: 0">
                    <p>{{this.updateTypeFilter(twoConfirm.updateType)}}</p>
                </FormItem>
                <FormItem label="灰度发布" style="margin-bottom: 0">
                    <p>{{this.grayReleasedFilter(twoConfirm.grayReleased)}}</p>
                </FormItem>
                <FormItem label="创建人" style="margin-bottom: 0">
                    <p>{{twoConfirm.createdBy}}</p>
                </FormItem>
                <FormItem label="上架确认" prop="twoAppVersion">
                    <Input v-model="twoConfirm.twoAppVersion" placeholder="请输入当前上架版本以上线"/>
                </FormItem>
                <FormItem align="right" style="margin-bottom: 0">
                    <Button @click="twoConfirmCancel" style="margin-right: 12px;">取消</Button>
                    <Button type="primary" @click="twoConfirmSubmit('twoConfirm')">确认上架</Button>
                </FormItem>
            </Form>
            <div slot="footer"></div>
        </Modal>
        <Spin size="large" fix v-if="inLoading"></Spin>
    </div>
</template>
<script>
import { formatDate, http } from '@/libs/util';
const versionStatus = [
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
        const validateTwoAppVersion = (rule, value, callback) => {
            if (value !== this.twoConfirm.appVersion) {
                callback(new Error('输入的版本号不符,请重试'));
            } else {
                callback();
            }
        };
        return {
            inLoading: true,
            // 分页
            currentPage: 1,
            total: 0,
            pageSize: 10,
            // 类型筛选
            updateTypes: [
                {
                    label: '强制更新',
                    value: 0
                },
                {
                    label: '一般更新',
                    value: 1
                },
                {
                    label: '静默更新',
                    value: 2
                },
                {
                    label: '可忽略更新',
                    value: 3
                },
                {
                    label: '静默可忽略更新',
                    value: 4
                }
            ],
            // table
            columns: [
                {
                    title: '#',
                    key: 'id',
                    fixed: 'left',
                    width: 80
                },
                {
                    title: '版本号',
                    key: 'appVersion',
                    width: 140,
                    fixed: 'left'
                },
                {
                    title: '允许最低版本',
                    minWidth: 140,
                    key: 'allowLowestVersion'
                },
                {
                    title: '更新类型',
                    minWidth: 140,
                    render: (h, params) => {
                        return h('div', this.updateTypeFilter(params.row.updateType));
                    }
                },
                {
                    title: '更新描述',
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
                                        this.$Modal.confirm({
                                            width: '800',
                                            cancelText: ' ',
                                            render: (h) => {
                                                return h('div', [
                                                    h('h2', {
                                                        style: {
                                                            fontSize: '20px',
                                                            marginBottom: '10px',
                                                            marginTop: '-20px',
                                                            borderBottom: '1px solid #f7f7f7',
                                                            paddingBottom: '10px'
                                                        }
                                                    }, '更新描述'),
                                                    h('pre', {
                                                        style: {
                                                            overflow: 'hidden',
                                                            overflowX: 'auto'
                                                        }
                                                    }, params.row.versionDescription)
                                                ]);
                                            }
                                        });
                                    }
                                }
                            }, '查看')
                        ]);
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
                                    value: !(params.row.versionStatus === 0 || params.row.versionStatus === 2)
                                },
                                on: {
                                    'on-change': (value) => {
                                        if (value === true) {
                                            this.twoConfirm = {
                                                id: params.row.id,
                                                appVersion: params.row.appVersion,
                                                allowLowestVersion: params.row.allowLowestVersion,
                                                updateType: params.row.updateType,
                                                grayReleased: params.row.grayReleased,
                                                createdBy: params.row.createdBy
                                            };
                                            this.inTwoConfirm = true;
                                        } else {
                                            let status = params.row.versionStatus === 0 || params.row.versionStatus === 2 ? 'delivery' : 'undelivery';
                                            this.putChangeStatus(params.row.id, status);
                                        }
                                    }
                                }
                            })
                        ]);
                    }
                },
                {
                    title: '灰度发布',
                    minWidth: 140,
                    render: (h, params) => {
                        return h('div', this.grayReleasedFilter(params.row.grayReleased));
                    }
                },
                {
                    title: '添加时间',
                    width: 180,
                    render: (h, params) => {
                        var date = new Date(params.row.createdTime);
                        return h('div', formatDate(date, 'yyyy-MM-dd hh:mm:ss'));
                    }
                },
                {
                    title: '添加者',
                    key: 'createdBy',
                    minWidth: 140
                },
                {
                    title: '操作',
                    width: 220,
                    fixed: 'right',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    type: 'success',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '12px'
                                },
                                on: {
                                    click: () => {
                                        this.$router.push({
                                            name: 'version-android_apk',
                                            params: {
                                                androidId: params.row.id
                                            }
                                        });
                                    }
                                }
                            }, '包管理'),
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
                                        this.$router.push({
                                            name: 'version-android_edit',
                                            params: {
                                                androidId: params.row.id
                                            }
                                        });
                                    }
                                }
                            }, '编辑'),
                            h('Poptip', {
                                props: {
                                    confirm: true,
                                    transfer: true,
                                    width: 260,
                                    placement: 'top-end',
                                    title: '确定删除[' + params.row.appVersion + ']版本吗？'
                                },
                                on: {
                                    'on-ok': (value) => {
                                        this.delIOS(params.row.id);
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
                appVersion: '',
                updateType: null,
                versionStatus: null
            },
            versionStatus,
            inTwoConfirm: false,
            twoConfirm: {
                id: '',
                appVersion: '',
                allowLowestVersion: '',
                updateType: '',
                grayReleased: '',
                createdBy: '',
                twoAppVersion: ''
            },
            twoConfirmRule: {
                twoAppVersion: [
                    { required: true, message: '请输入当前上架的版本号', trigger: 'blur' },
                    { required: true, validator: validateTwoAppVersion, trigger: 'blur' }
                ]
            }
        };
    },
    created () {
        this.getAndroids();
    },
    methods: {
        async getAndroids () {
            this.inLoading = true;
            let response = await http.get('/android', {
                params: {
                    page: this.currentPage,
                    pageSize: this.pageSize,
                    appVersion: this.queryParams.appVersion,
                    updateType: this.queryParams.updateType === undefined ? null : this.queryParams.updateType,
                    versionStatus: this.queryParams.versionStatus === undefined ? null : this.queryParams.versionStatus
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
        async delIOS (id) {
            let response = await http.delete('/android/' + id);
            if (response.data.code === 200) {
                this.$Notice.success({
                    title: '请求成功',
                    desc: `删除版本[${response.data.data.appVersion}]成功`
                });
            } else {
                this.$Notice.error({
                    title: '请求失败',
                    desc: response.data.message
                });
            }

            this.getAndroids();
        },
        async putChangeStatus (id, status) {
            let response = await http.put('/android/' + id + '/' + status);
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

            this.getAndroids();
        },
        searchAndroids () {
            this.currentPage = 1;
            this.getAndroids();
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getAndroids();
        },
        toCreatePage () {
            this.$router.push({
                name: 'version-android_create'
            });
        },
        updateTypeFilter (num) {
            if (isNaN(num)) return '一般更新';

            let word;
            switch (num) {
                case 0:
                    word = '强制更新';
                    break;
                case 1:
                    word = '一般更新';
                    break;
                case 2:
                    word = '静默更新';
                    break;
                case 3:
                    word = '可忽略更新';
                    break;
                case 4:
                    word = '静默可忽略更新';
                    break;
                default:
                    word = '一般更新';
            }
            return word;
        },
        grayReleasedFilter (num) {
            if (isNaN(num)) return '全量发布';

            let word;
            switch (num) {
                case 0:
                    word = '全量发布';
                    break;
                case 1:
                    word = '白名单发布';
                    break;
                case 2:
                    word = 'IP 发布';
                    break;
                default:
                    word = '全量发布';
            }
            return word;
        },
        twoConfirmCancel () {
            let item = this.tableList.find(item => {
                return item.id === this.twoConfirm.id;
            });
            this.getAndroids();
            this.inTwoConfirm = false;
        },
        twoConfirmSubmit (name) {
            this.$refs[name].validate((valid) => {
                if (!valid) {
                    this.$Message.error('请先完成所有必填项内容!');
                    return false;
                }

                this.putChangeStatus(this.twoConfirm.id, 'delivery');
                this.inTwoConfirm = false;
            });
        }
    },
    watch: {
        'currentPage': 'getAndroids'
    }
};
</script>
<style scoped lang="scss">
    #version-android{
        .form{
            margin-bottom: 20px;
        }
    }
</style>
