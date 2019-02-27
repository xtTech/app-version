<template>
	<div id="rn-package">
		<Card>
			<div class="form">
				<Form :model="queryParams" :label-width="120">
					<Row>
						<Col span="12">
							<FormItem label="操作的资源">
								<Row :gutter="6" type="flex">
									<Col span="12">
										<Select v-model="queryParams.operationResource" filterable clearable @on-change="getLogResourceType">
											<OptionGroup v-for="item in resourceList" :key="item.name" :label="item.name">
												<Option v-for="data in item.datas" :value="data.key" :key="data.key" :label="data.value" />
											</OptionGroup>
										</Select>
									</Col>
									<Col span="12">
										<Select v-model="queryParams.operationType" filterable clearable>
											<Option v-for="item in resourceTypeList" :key="item.key" :value="item.value" :label="item.value" />
										</Select>
									</Col>
								</Row>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label="操作者手机号">
								<Input v-model="queryParams.phone" placeholder=""/>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem label="应用">
								<Select v-model="queryParams.appId" filterable clearable>
									<Option v-for="item in appList" :value="item.id" :key="item.id" :label="item.appName">
										<span>{{item.appName}}</span>
										<span style="float:right;color:#ccc">{{item.tenantAppId}}</span>
									</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label="操作者用户名">
								<Input v-model="queryParams.nickName" placeholder=""/>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem label="时间范围">
								<DatePicker @on-change="onTimeChange" type="datetimerange" clearable placeholder="选择一个时间范围进行查询" style="width: 100%"></DatePicker>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="24" style="text-align: right">
							<Button type="primary" icon="md-search" @click="searchLogs">搜索相关日志</Button>
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
		<Modal v-model="inViewContent" :mask-closable="false" :width="800" cancel-text=" " :closable="true" title="详情" class="viewcode">
			<div v-if="inViewContent">
				<div ref="content"></div>
			</div>
		</Modal>
		<Spin size="large" fix v-if="inSearch"></Spin>
	</div>
</template>
<script>
import { http, fmtFileSize, formatDate } from '@/libs/util';
import { getApps } from '@/libs/account';

import jsbeautifier from 'js-beautify';
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
            inSearch: false,
            total: 0,
            pageSize: 10,
            currentPage: 1,
            queryParams: {
                operationType: '',
                operationResource: '',
                phone: '',
                appId: '',
                nickName: '',
                startDate: '',
                endDate: ''
            },
            columns: [
                {
                    title: '#',
                    key: 'id',
                    width: 80
                },
                {
                    title: '操作者昵称',
                    key: 'nickName',
                    minWidth: 100,
                    width: 120
                },
                {
                    title: '操作者手机号',
                    key: 'phone',
                    minWidth: 100,
                    width: 120
                },
                {
                    title: '被操作的应用',
                    width: 140,
                    render: (h, params) => {
                        let app = this.appList.find(app => {
                            return app.id === params.row.appId;
                        });
                        return h('div', [h('span', app.appName)]);
                    }
                },
                {
                    title: '操作类型',
                    width: 90,
                    align: 'center',
                    render: (h, params) => {
                        let type = this.operationTypeFilter(params.row.operationType);
                        return h('span', type);
                    }
                },
                {
                    title: '操作资源',
                    minWidth: 130,
                    render: (h, params) => {
                        let type = this.operationResourceFilter(
                            params.row.operationResource
                        );
                        return h('span', type);
                    }
                },
                {
                    title: '操作描述',
                    key: 'operationDescription',
                    width: 150
                },
                {
                    title: '操作结果',
                    key: 'operationResult',
                    width: 100,
                    render: (h, params) => {
                        return h(
                            'span',
                            params.row.operationResult === 'SUCCESS' ? '成功' : '失败'
                        );
                    }
                },
                {
                    title: '操作时间',
                    minWidth: 180,
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
                    title: '操作详情',
                    width: 90,
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
                                            this.viewContent = params.row.operationContent;
                                            this.inViewContent = true;
                                        }
                                    }
                                },
                                '查看'
                            )
                        ]);
                    }
                }
            ],
            tableList: [],
            logTypeList: [],
            resourceList: [],
            resourceTypeList: [],
            appList: [],
            inViewContent: false,
            viewContent: ''
        };
    },
    async created () {
        this.inSearch = true;
        await this.getLogType();
        await this.getLogResource();
        await this.getApps();
        await this.getLogs();
        this.inSearch = false;
    },
    methods: {
        async getLogs () {
            this.inSearch = true;

            let response = await http.get('/log', {
                params: {
                    page: this.currentPage,
                    pageSize: this.pageSize,
                    operationType: this.queryParams.operationType,
                    operationResource: this.queryParams.operationResource,
                    phone: this.queryParams.phone,
                    appId: this.queryParams.appId,
                    nickName: this.queryParams.nickName,
                    startDate: this.queryParams.startDate,
                    endDate: this.queryParams.endDate
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
        // 操作类型显示
        async getLogType () {
            let response = await http.get('/log/type');
            if (response.data.code === 200) {
                let keys = Object.keys(response.data.data);
                keys.forEach(key => {
                    this.logTypeList.push({
                        key,
                        value: response.data.data[key]
                    });
                });
            }
        },
        // 操作资源类型显示
        async getLogResource () {
            this.resourceList = [];
            let response = await http.get('/log/resource');
            if (response.data.code === 200) {
                let keys = Object.keys(response.data.data);
                let datas = [
                    {
                        name: '版本管理',
                        datas: []
                    },
                    {
                        name: 'RN 管理',
                        datas: []
                    },
                    {
                        name: '自定义接口',
                        datas: []
                    },
                    {
                        name: '管理员',
                        datas: []
                    },
                    {
                        name: '其他',
                        datas: []
                    }
                ];
                let keyGroups = {
                    APK: '版本管理',
                    CHANNEL: '版本管理',
                    ANDROID_VERSION: '版本管理',
                    IOS_VERSION: '版本管理',
                    RN_PACKAGE: 'RN 管理',
                    RN_ROUTE: 'RN 管理',
                    CUSTOM_API: '自定义接口',
                    APP: '管理员',
                    USER: '管理员',
                    USER_APP_REL: '管理员'
                };
                keys.forEach(key => {
                    let dataKey = keyGroups[key] == null ? '其他' : keyGroups[key];
                    let data = datas.find(data => {
                        return data.name === dataKey;
                    });
                    data.datas.push({
                        key,
                        value: response.data.data[key]
                    });
                });
                this.resourceList = datas;
            }
        },
        // 操作资源类型显示
        async getLogResourceType (key) {
            this.queryParams.operationType = '';
            this.resourceTypeList = [];
            let response = await http.get(`/log/${key}/type`);
            if (response.data.code === 200) {
                let keys = Object.keys(response.data.data);
                let datas = [];
                keys.forEach(key => {
                    datas.push({
                        key: key,
                        value: response.data.data[key]
                    });
                });
                this.resourceTypeList = datas;
            }
        },
        async getApps () {
            let response = await http.get('/admin/app/list', {
                params: {
                    page: 1,
                    isAll: true,
                    pageSize: 9999
                }
            });
            if (response.data.code === 200) {
                this.appList = response.data.data.records;
            }
        },
        searchLogs () {
            this.currentPage = 1;
            this.getLogs();
        },
        changePageSize (size) {
            this.pageSize = size;
            this.getLogs();
        },
        operationTypeFilter (val) {
            let type = this.logTypeList.find(type => {
                return type.key === val;
            });
            return type != null ? type.value : type;
        },
        operationResourceFilter (val) {
            let type;
            this.resourceList.forEach(data => {
                data.datas.forEach(k => {
                    if (k.key === val) {
                        type = k.value;
                    }
                });
            });
            return type != null ? type : val;
        },
        onTimeChange (time) {
            this.queryParams.startDate = time[0];
            this.queryParams.endDate = time[1];
        }
    },
    watch: {
        currentPage: 'getLogs',
        inViewContent (status) {
            if (status === true) {
                this.$nextTick(() => {
                    new CodeMirror(this.$refs.content, {
                        value: jsbeautifier(this.viewContent),
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
                this.viewContent = '';
            }
        }
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
