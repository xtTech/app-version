<template>
    <div id="admin-app">
        <Card>
            <Form :model="queryParams" :label-width="80">
                <Row>
                    <Col span="6">
                        <FormItem label="搜索用户">
                            <Input v-model="queryParams.phone" placeholder="手机号"/>
                        </FormItem>
                    </Col>
                    <Col span="18">
                        <Button type="primary" icon="md-search" @click="searchUsers" style="margin: 0 14px">搜索用户</Button>
                    </Col>
                </Row>
            </Form>
            <Table border :columns="columns" :data="tableList"/>
            <div style="margin: 10px 0 0 0; text-align: right">
                <Page show-total show-elevator show-sizer :total="total" :page-size="pageSize"
                      :current.sync="currentPage" @on-page-size-change="changePageSize"/>
            </div>
        </Card>
        <Modal v-model="inBind" :mask-closable="false" :title="bindTitle" class="admin-user-modal">
            <bind-app v-if="inBind" :user-id="userID" @close-edit-modal="handelCloseBind"></bind-app>
            <div slot="footer"></div>
        </Modal>
        <Spin size="large" fix v-if="inLoading"></Spin>
    </div>
</template>
<script>
import { http, formatDate } from '@/libs/util';
import bindApp from './bindApp';
export default {
    data () {
        return {
            inLoading: true,
            // 分页
            currentPage: 1,
            total: 0,
            pageSize: 10,
            queryParams: {
                phone: ''
            },
            // table
            columns: [
                {
                    title: '手机号',
                    key: 'phone',
                    fixed: 'left',
                    width: 130
                },
                {
                    title: '用户ID',
                    key: 'userId',
                    minWidth: 200
                },
                {
                    title: '用户昵称',
                    key: 'nickName',
                    minWidth: 140
                },
                {
                    title: '首次登录时间',
                    minWidth: 180,
                    render: (h, params) => {
                        return h(
                            'span',
                            formatDate(
                                new Date(params.row.firstLoginTime),
                                'yyyy-MM-dd hh:mm:ss'
                            )
                        );
                    }
                },
                {
                    title: '操作',
                    width: 180,
                    fixed: 'right',
                    render: (h, params) => {
                        return h('div', [
                            h(
                                'Poptip',
                                {
                                    attrs: {
                                        placement: 'left',
                                        width: '400',
                                        transfer: true
                                    }
                                },
                                [
                                    h(
                                        'Button',
                                        {
                                            props: {
                                                type: 'success',
                                                size: 'small'
                                            },
                                            style: {
                                                marginRight: '5px'
                                            }
                                        },
                                        '修改昵称'
                                    ),
                                    h(
                                        'div',
                                        {
                                            slot: 'content',
                                            style: {
                                                margin: '4px'
                                            }
                                        },
                                        [
                                            h('Input', {
                                                attrs: {
                                                    search: true,
                                                    'enter-button': '修改',
                                                    value: '',
                                                    placeholder: '请输入要修改的昵称'
                                                },
                                                on: {
                                                    'on-search': nickName => {
                                                        if (!/^[-\u4e00-\u9fa5_a-zA-Z0-9]+$/.test(nickName)) {
                                                            this.$Notice.warning({
                                                                title: '用户昵称中只允许包含字母、数字、中文、中划线-和下划线_'
                                                            });
                                                        } else if (nickName.length < 2 || nickName.length > 20) {
                                                            this.$Notice.warning({
                                                                title: '用户昵称长度只允许 2 - 20个字符'
                                                            });
                                                        } else {
                                                            this.putAdminUser(nickName, params.row.userId);
                                                        }
                                                    }
                                                }
                                            }),
                                            h(
                                                'p',
                                                { style: { margin: '10px 0 0 0' } },
                                                '用户昵称中只允许包含字母、数字和中文'
                                            )
                                        ]
                                    )
                                ]
                            ),
                            h(
                                'Button',
                                {
                                    props: {
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.bindApp(params.row.userId, params.row.phone);
                                        }
                                    }
                                },
                                '绑定应用'
                            )
                        ]);
                    }
                }
            ],
            tableList: [],
            inBind: false,
            userID: 0,
            bindTitle: ''
        };
    },
    components: { bindApp },
    created () {
        this.getUsers();
    },
    methods: {
        async getUsers () {
            this.inLoading = true;
            let response = await http.get('/admin/user/list', {
                params: {
                    page: this.currentPage,
                    pageSize: this.pageSize,
                    phone: this.queryParams.phone
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
        async putAdminUser (nickName, userId) {
            this.inLoading = true;
            let response = await http.put('/admin/user', {
                nickName,
                userId
            });
            this.inLoading = false;

            if (response.data.code === 200) {
                document.body.click();
                this.getUsers();
            } else {
                this.$Notice.warning({
                    title: response.data.message
                });
            }
        },
        bindApp (userID, phone) {
            this.inBind = true;
            this.userID = userID;
            this.bindTitle = `正在为用户 [${phone}] 绑定应用`;
        },
        searchUsers () {
            this.currentPage = 1;
            this.getUsers();
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getUsers();
        },
        handelCloseBind () {
            this.inBind = false;
            this.getUsers();
        }
    },
    watch: {
        currentPage: 'getUsers'
    }
};
</script>
<style lang="scss">
.admin-user-modal {
	.ivu-modal {
		width: 80% !important;
		max-width: 900px;
	}
	.ivu-modal-footer {
		display: none;
	}
}
</style>
