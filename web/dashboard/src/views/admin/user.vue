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
						<Button type="default" icon="md-add" @click="openUserModal(0)">添加用户</Button>
                    </Col>
                </Row>
            </Form>
            <Table border :columns="columns" :data="tableList"/>
            <div style="margin: 10px 0 0 0; text-align: right">
                <Page show-total show-elevator show-sizer :total="total" :page-size="pageSize"
                      :current.sync="currentPage" @on-page-size-change="changePageSize"/>
            </div>
        </Card>
		
		
		<!-- 添加用户的对话框 -start -->
		<Modal v-model="modal" :title="(modalEditId === 0 ? '添加' : '编辑') + '用户'"
		       loading
		       :closable="false"
		       :mask-closable="false">
		    <Form ref="modelFormRule" :model="modelForm" :rules="modelFormRule">
		        <FormItem label="手机号" prop="phone">
		            <Input v-model="modelForm.phone" placeholder="" />
		        </FormItem>
		        <FormItem label="用户名" prop="username">
		            <Input v-model="modelForm.username" placeholder="" :disabled="modalEditId > 0"/>
		        </FormItem>
				<FormItem label="密码" prop="password" v-show="modalEditId==0" >
				    <Input type="password" v-model="modelForm.password" placeholder="" :disabled="modalEditId > 0"  />
				</FormItem>
				<FormItem label="昵称" prop="nickName">
				    <Input v-model="modelForm.nickName" placeholder="" :disabled="modalEditId > 0" />
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
		<!-- 添加用户的对话框 -end -->
		
		
		
		<!-- 重置密码的对话框 -start -->
		<Modal v-model="restPwdModal" title="重置密码"
		       loading
		       :closable="false"
		       :mask-closable="false">
		    <Form ref="modelFormRule" :model="modelForm" :rules="modelFormRule">
				<FormItem label="密码" prop="password" >
				    <Input type="password" v-model="modelForm.password" placeholder="" />
				</FormItem>
		    </Form>
		    <Spin size="large" fix v-if="inRestPwdModalLoading">
		        <Icon type="load-c" size=18 class="spin-icon-load"></Icon>
		        <div>正在加载...</div>
		    </Spin>
		    <div class="footer" slot="footer">
		        <Button type="default" @click="handleResetRestPwdModal('modelFormRule')">取消</Button>
		        <Button type="primary" @click="restPwdModalSubmit('modelFormRule')">保存</Button>
		    </div>
		</Modal>
		<!-- 重置密码的对话框 -end -->
		
		
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
import md5 from 'js-md5';
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
                    width: 120
                },
                {
                    title: '用户名',
                    key: 'username',
                    minWidth: 50
                },
                {
                    title: '用户昵称',
                    key: 'nickName',
                    minWidth: 50
                },
                {
                    title: '首次登录时间',
                    minWidth: 100,
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
                    width: 400,
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
							       this.openUserModal(params.row.userId);
							     }
							   }
							}, '编辑'),
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
							       this.openRestPwdModal(params.row.userId);
							     }
							   }
							}, '重置密码'),
							
							h('Poptip', {
							    props: {
							        confirm: true,
							        transfer: true,
							        width: 260,
							        placement: 'top-end',
							        title: '确定删除[' + params.row.username + ']吗？'
							    },
							    on: {
							        'on-ok': (value) => {
							            this.delUser(params.row.userId);
							        }
							    }
							}, [
							    h('Button', {
							        props: {
							            type: 'error',
							            size: 'small'
							        }
							    }, '删除')
							]),
							
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
            bindTitle: '',
			modal: false,
			inModalLoading: false,
			restPwdModal: false,
			inRestPwdModalLoading: false,
			modalEditId: 0,
			modelForm: {
			    phone:'',
			    username:'',
			    password:'',
			    nickName:''
			},
			modelFormRule: {
			    phone: [
			        { required: true, message: '请输入手机号', trigger: 'blur' },
			        { required: true, validator: validateInput, trigger: 'blur' }
			    ],
			    username: [
			        { required: true, message: '请输入用户名', trigger: 'blur' },
			        { required: true, validator: validateInput, trigger: 'blur' }
			    ],
				password: [
				    { required: true, message: '请输入密码', trigger: 'blur' },
					{ required: true, validator: validateInput, trigger: 'blur' }
				],
				nickName: [
				    { required: true, message: '请输入昵称', trigger: 'blur' },
					{ required: true, validator: validateInput, trigger: 'blur' }
				]
			}
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
                this.tableList = response.data.record.records;
                this.total = response.data.record.total;
                this.currentPage = response.data.record.current;
            } else {
                this.$Notice.error({
                    title: '请求失败',
                    desc: response.data.info
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
                this.getUser();
            } else {
                this.$Notice.warning({
                    title: response.data.info
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
        },
		openUserModal (id = 0) {
		    this.modalEditId = id;
		    this.modal = true;
		    if (this.modalEditId !== 0) {
				this.getUser();
		        this.inModalLoading = true;
		    }
		},
		openRestPwdModal (id) {
			this.modalEditId = id;
		    this.restPwdModal = true;
		    //this.inRestPwdModalLoading = true;
		},
		
		async getUser () {
		    let response = await http.get('/user/getUser/' + this.modalEditId);
		
		    if (response.data.code === 200) {
		        this.modelForm.phone = response.data.record.phone;
		        this.modelForm.username = response.data.record.username;
		        this.modelForm.password = response.data.record.password;
		        this.modelForm.nickName = response.data.record.nickName;
				
		    }
		
		    this.inModalLoading = false;
		},
		
		handleReset (name) {
		    this.$refs[name].resetFields();
		    this.modal = false;
		    this.getUsers();
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
		                    desc: '添加[' + response.record.username + ']成功'
		                });
		
		                this.handleReset(name);
		            } else {
		                this.$Notice.error({
		                    title: '请求失败',
		                    desc: response.info
		                });
		            }
		        };
		
		        if (this.modalEditId === 0) {
		            this.addUser().then(message);
		        } else {
		            this.putUser().then(message);
		        }
		    });
		},
		
		
		handleResetRestPwdModal (name) {
		    this.$refs[name].resetFields();
		    this.restPwdModal = false;
		    this.getUsers();
		},
		restPwdModalSubmit(name){
			this.$refs[name].validate((valid) => {
			    if (!valid) {
			        this.$Message.error('请先完成所有必填项内容!');
			        return false;
			    }
					
			    const message = response => {
			        if (response.code === 200) {
			            this.$Notice.success({
			                title: '请求成功',
			                desc: '用户[' + response.record.username + ']密码重置成功'
			            });
					
			            this.handleResetRestPwdModal(name);
			        } else {
			            this.$Notice.error({
			                title: '请求失败',
			                desc: response.info
			            });
			        }
			    };
				
			    this.restPwd().then(message); //密码重置成功
				
			});
		},
		async addUser () {
		    let query = {
		        phone: this.modelForm.phone.trim(),
		        username: this.modelForm.username.trim(),
				password: md5(this.modelForm.password.trim()),
				nickName: this.modelForm.nickName.trim()
		    };
		    let response = await http.post('/user/addUser', query);
		    return response.data;
		},
		async putUser () {
		    let response = await http.put('/user/editUser/' + this.modalEditId, {
				phone: this.modelForm.phone.trim(),
				username: this.modelForm.username.trim(),
				nickName: this.modelForm.nickName.trim()
		    });
		    return response.data;
		},
		async restPwd () {
		    let response = await http.put('/user/restPwd/' + this.modalEditId, {
				password: md5(this.modelForm.password.trim())
		    });
		    return response.data;
		},
		async delUser (id) {
		    let response = await http.delete('/user/delUser/' + id);
		    if (response.data.code === 200) {
		        this.$Notice.success({
		            title: '请求成功',
		            desc: `删除成功`
		        });
		    } else {
		        this.$Notice.error({
		            title: '请求失败',
		            desc: response.data.info
		        });
		    }
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
