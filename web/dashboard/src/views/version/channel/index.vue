<template>
    <div>
        <Card>
            <Form :model="queryParams" :label-width="80">
                <Row>
                    <Col span="6">
                        <FormItem label="渠道名称">
                            <Input v-model="queryParams.channelName" placeholder=""/>
                        </FormItem>
                    </Col>
                    <Col span="6">
                        <FormItem label="渠道码">
                            <Input v-model="queryParams.channelCode" placeholder=""/>
                        </FormItem>
                    </Col>
                    <Col span="12" style="margin-top: 1px;">
                        <Button type="primary" icon="md-search" @click="searchCannel" style="margin: 0 14px">搜索</Button>
                        <Button type="default" icon="md-add" @click="openModal(0)">添加渠道</Button>
                    </Col>
                </Row>
            </Form>
            <Table border :columns="columns" :data="tableList"/>
            <div style="margin: 10px 0 0 0; text-align: right">
                <Page show-total show-elevator show-sizer :total="total" :page-size="pageSize"
                          :current.sync="currentPage" @on-page-size-change="changePageSize"/>
            </div>
        </Card>
        <Modal v-model="modal" :title="(modalEditId === 0 ? '添加' : '编辑') + '渠道'"
               loading
               :closable="false"
               :mask-closable="false">
            <Form ref="modelFormRule" :model="modelForm" :rules="modelFormRule">
                <FormItem label="渠道名称" prop="channelName">
                    <Input v-model="modelForm.channelName" placeholder=""/>
                </FormItem>
                <FormItem label="渠道码" prop="channelCode">
                    <Input v-model="modelForm.channelCode" placeholder="" :disabled="modalEditId > 0"/>
                </FormItem>
            </Form>
            <Spin size="large" fix v-if="inModalLoading">
                <Icon type="load-c" size=18 class="spin-icon-load"></Icon>
                <div>正在加载...</div>
            </Spin>
            <div class="footer" slot="footer">
                <Button type="default" @click="handleReset('modelFormRule')">取消</Button>
                <Button type="primary" @click="modalSubmit('modelFormRule')">{{modalEditId === 0 ? '添加' : '编辑'}}</Button>
            </div>
        </Modal>
        <Spin size="large" fix v-if="inLoading"></Spin>
    </div>
</template>
<script>
import { formatDate, http } from '@/libs/util';

export default {
    data () {
        const validateInput = (rule, value, callback) => {
            if (value !== value.trim()) {
                callback(new Error('请移除前后空格'));
            } else {
                callback();
            }
        };
        return {
            currentPage: 1,
            total: 0,
            pageSize: 10,
            inLoading: true,
            columns: [
                {
                    title: '#',
                    key: 'id',
                    fixed: 'left',
                    width: 80
                },
                {
                    title: '渠道名称',
                    width: 170,
                    fixed: 'left',
                    key: 'channelName'
                },
                {
                    title: '渠道码',
                    minWidth: 140,
                    key: 'channelCode'
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
                    title: '状态',
                    minWidth: 140,
                    align: 'center',
                    render: (h, params) => {
                        if (params.row.channelStatus === 1) {
                            return h('div', '正常');
                        } else {
                            return h('div', '已废弃');
                        }
                    }
                },
                {
                    title: '操作',
                    width: 200,
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
                                    click: () => {
                                        this.openModal(params.row.id);
                                    }
                                }
                            }, '编辑'),
                            h('Poptip', {
                                props: {
                                    confirm: true,
                                    transfer: true,
                                    width: 260,
                                    placement: 'top-end',
                                    title: '确定废弃[' + params.row.channelName + ']吗？'
                                },
                                style: {
                                    marginRight: '5px',
                                    display: params.row.channelStatus === 1 ? 'inline-block' : 'none'
                                },
                                on: {
                                    'on-ok': (value) => {
                                        this.putChangeStatus(params.row.id, 'scrap');
                                    }
                                }
                            }, [
                                h('Button', {
                                    props: {
                                        type: 'warning',
                                        size: 'small'
                                    }
                                }, '废弃')
                            ]),
                            h('Button', {
                                props: {
                                    type: 'success',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px',
                                    display: params.row.channelStatus === 2 ? 'inline-block' : 'none'
                                },
                                on: {
                                    'click': (value) => {
                                        this.putChangeStatus(params.row.id, 'open');
                                    }
                                }
                            }, '启用'),
                            h('Poptip', {
                                props: {
                                    confirm: true,
                                    transfer: true,
                                    width: 260,
                                    placement: 'top-end',
                                    title: '确定删除[' + params.row.channelName + ']吗？'
                                },
                                on: {
                                    'on-ok': (value) => {
                                        this.deleteChannel(params.row.id);
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
            // 查询参数
            queryParams: {
                channelName: '',
                channelCode: ''
            },
            editData: {},
            modal: false,
            inModalLoading: false,
            modalEditId: 0,
            modelForm: {
                channelName: '',
                channelCode: '',
                channelType: '',
                channelStatus: ''
            },
            modelFormRule: {
                channelName: [
                    { required: true, message: '请输入渠道名称', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ],
                channelCode: [
                    { required: true, message: '请输入渠道码', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ]
            }
        };
    },
    created () {
        this.getChannels();
    },
    methods: {
        async getChannel () {
            let response = await http.get('/channel/' + this.modalEditId);

            if (response.data.code === 200) {
                this.modelForm.channelName = response.data.data.channelName;
                this.modelForm.channelCode = response.data.data.channelCode;
                this.modelForm.channelType = response.data.data.channelType;
                this.modelForm.channelStatus = response.data.data.channelStatus;
            }

            this.inModalLoading = false;
        },
        async getChannels () {
            this.inLoading = true;
            let response = await http.get('/channel', {
                params: {
                    page: this.currentPage,
                    pageSize: this.pageSize,
                    channelName: this.queryParams.channelName,
                    channelCode: this.queryParams.channelCode
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
        async addChannel () {
            let query = {
                channelName: this.modelForm.channelName,
                channelCode: this.modelForm.channelCode
            };
            let response = await http.post('/channel', query);
            return response.data;
        },
        async putChannel () {
            let response = await http.put('/channel/' + this.modalEditId + '/edit', {
                channelName: this.modelForm.channelName,
                channelCode: this.modelForm.channelCode,
                channelType: this.modelForm.channelType,
                channelStatus: this.modelForm.channelStatus
            });

            return response.data;
        },
        async putChangeStatus (id, status) {
            let response = await http.put('/channel/' + id + '/' + status);
            let statusText = status === 'scrap' ? '废弃' : '启用';

            if (response.data.code === 200) {
                this.$Notice.success({
                    title: '请求成功',
                    desc: `${statusText}'${response.data.data.channelName}'渠道成功`
                });
            } else {
                this.$Notice.error({
                    title: '请求失败',
                    desc: response.data.message
                });
            }

            this.getChannels();
        },
        async deleteChannel (id) {
            let response = await http.delete(`/channel/${id}`);
            if (response.data.code !== 200) {
                this.$Notice.error({
                    title: '错误',
                    desc: response.data.message
                });
            } else {
                this.$Message.success('删除成功!');
            }
            this.getChannels();
        },
        searchCannel () {
            this.currentPage = 1;
            this.getChannels();
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getChannels();
        },
        openModal (id = 0) {
            this.modalEditId = id;
            this.modal = true;

            if (this.modalEditId !== 0) {
                this.inModalLoading = true;
                this.getChannel();
            }
        },
        handleReset (name) {
            this.$refs[name].resetFields();
            this.modal = false;
            this.getChannels();
        },
        modalSubmit (name) {
            this.$refs[name].validate((valid) => {
                if (!valid) {
                    this.$Message.error('请先完成所有必填项内容!');
                    return false;
                }

                const message = response => {
                    if (response.code === 200) {
                        this.$Notice.success({
                            title: '请求成功',
                            desc: '添加渠道[' + response.data.channelName + ']成功'
                        });

                        this.handleReset(name);
                    } else {
                        this.$Notice.error({
                            title: '请求失败',
                            desc: response.message
                        });
                    }
                };

                if (this.modalEditId === 0) {
                    this.addChannel().then(message);
                } else {
                    this.putChannel().then(message);
                }
            });
        }
    },
    watch: {
        'currentPage': 'getChannels'
    }
};
</script>
<style scoped>
    .footer{
        text-align: right;
    }
</style>
