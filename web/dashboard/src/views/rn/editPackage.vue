<script src="../../../../../../report/src/main.js"></script>
<template>
    <div id="rn-edit">
        <Form ref="editFormRule" :model="editForm" :rules="editFormRule" :label-width="110" style="position: relative">
            <FormItem label="模块名称" prop="rnName">
                <Input v-model="editForm.rnName" placeholder=""/>
            </FormItem>
            <FormItem label="通用昵称" prop="rnNickName">
                <Input v-model="editForm.rnNickName" placeholder=""/>
            </FormItem>
            <FormItem label="RN包版本号" prop="rnVersion">
                <Input v-model="editForm.rnVersion" placeholder=""/>
            </FormItem>
            <FormItem label="适用终端" prop="rnType">
                <RadioGroup v-model="editForm.rnType">
                    <Radio :label="2">{{ 2 | deviceFilter}}</Radio>
                    <Radio :label="1">{{ 1 | deviceFilter}}</Radio>
                </RadioGroup>
            </FormItem>
            <FormItem label="版本区间" prop="version">
                <Row>
                    <Col span="10">
                        <AutoComplete
                                v-model="editForm.versionMin"
                                clearable
                                :data="versionList"
                                :filter-method="versionFilter"
                                placeholder="输入或选择版本">
                        </AutoComplete>
                    </Col>
                    <Col span="4" style="text-align: center">≤ {{ editForm.rnType | deviceFilter}} ＜</Col>
                    <Col span="10">
                        <AutoComplete
                                v-model="editForm.versionMax"
                                clearable
                                :data="versionList"
                                :filter-method="versionFilter"
                                placeholder="输入或选择版本">
                        </AutoComplete>
                    </Col>
                </Row>
            </FormItem>
            <FormItem label="上传RN包">
                <Upload action="#"
                        :before-upload="handleUpload"
                        v-if="fileItem.name === ''"
                        type="drag">
                    <div style="padding: 20px 0">
                        <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                        <p>轻击或拖曳 RN 包文件至此上传</p>
                    </div>
                </Upload>
                <div v-else class="file-div">
                    <div>
                        {{fileItem.name}}
                        <Tag color="blue">正在上传</Tag>
                        <Button type="text" @click="stopUploadFile">移除</Button>
                    </div>
                    <Progress status="active" :percent="fileItem.progress"></Progress>
                </div>
            </FormItem>
            <FormItem label="RN包资源地址" prop="resourceUrl">
                <Input v-model="editForm.resourceUrl" readonly placeholder=""/>
            </FormItem>
            <FormItem label="RN包大小">
                <span>{{ fmtFileSize(editForm.rnSize) }}</span>
            </FormItem>
            <FormItem label="更新日志" prop="rnUpdateLog">
                <Input v-model="editForm.rnUpdateLog" type="textarea" :rows="4" placeholder=""/>
            </FormItem>
            <FormItem label="状态">
                <RadioGroup v-model="editForm.rnStatus">
                    <Radio :label="0">
                        <span>关闭</span>
                    </Radio>
                    <Radio :label="1">
                        <span>线上开启</span>
                    </Radio>
                    <Radio :label="2">
                        <span>测试需要</span>
                    </Radio>
                </RadioGroup>
            </FormItem>
            <Spin size="large" fix v-if="inLoading">
                <Icon type="load-c" size=18 class="spin-icon-load" style="margin-bottom: 12px"></Icon>
                <div>正在加载...</div>
            </Spin>
            <Row class="footer">
                <Col span="24">
                    <Button type="text" size="large" @click="handleReset('editFormRule')">取消</Button>
                    <Button type="primary" size="large" @click="handleSubmit('editFormRule')">提交</Button>
                </Col>
            </Row>
        </Form>
    </div>
</template>
<script>
    import { isLogin, getApp, getAppId, getApps, switchApp } from '@/libs/account';
	import {http, hasValue, fmtFileSize, getGUID, getFileName} from '@/libs/util';
	import uploadFileToOSS from '@/libs/oss';

	export default {
		name: 'rn-edit',
		props: {
			'rnId': {
				type: Number
			}
		},
		data() {
			const validateInput = (rule, value, callback) => {
				if (value !== value.trim()) {
					callback(new Error('请移除前后空格'));
				} else {
					callback();
				}
			};
			const validateType = (rule, value, callback) => {
				if (this.editForm.rnType !== 1 && this.editForm.rnType !== 2) {
					callback(new Error('请选择一个适用终端'));
				} else {
					callback();
				}
			};
			const validateVersion = (rule, value, callback) => {
				let versionList = this.editForm.rnType === 1 ? this.iosVersionList : this.androidVersionList;
				if (!hasValue(this.editForm.versionMin) || !hasValue(this.editForm.versionMax)) {
					callback(new Error('请选择设备适用版本'));
				} else if (this.editForm.versionMin !== this.editForm.versionMin.trim() || this.editForm.versionMax !== this.editForm.versionMax.trim()) {
					callback(new Error('请移除前后空格'));
                } else {
					callback();
				}
			};
			return {
                app: getApp(),
                appId: getAppId() ? getAppId() : '',
				inLoading: true,
				isEdit: false, // 是否为编辑状态，false 表示创建，true 编辑中
				editForm: {
					rnType: 2,
					rnName: '',
					rnNickName: '',
					rnVersion: '',
					versionMin: '',
					versionMax: '',
					resourceUrl: '',
					rnSize: 0,
					rnUpdateLog: '',
					rnStatus: 0
				},
				editFormRule: {
					rnName: [
						{required: true, message: '请输入模块名称', trigger: 'blur'},
						{required: true, validator: validateInput, trigger: 'blur'}
					],
					rnNickName: [
						{required: true, message: '请输入通用昵称', trigger: 'blur'},
						{required: true, validator: validateInput, trigger: 'blur'}
					],
					rnVersion: [
						{required: true, message: '请输入RN包版本号', trigger: 'blur'},
						{required: true, type: 'string', max: 32, message: '过长的版本号', trigger: 'blur'},
						{ required: true, pattern: /^([0]|[1-9][0-9]{0,5})\.([0]|[1-9][0-9]{0,5})\.([0]|[1-9][0-9]{0,5})\.([0]|[1-9][0-9]{0,5})$/g, message: '请输入符合规范的版本号，最大版本号为999999.999999.999999.999999', trigger: 'blur' },
						{required: true, validator: validateInput, trigger: 'blur'}
					],
					rnType: [
						{required: true, validator: validateType, trigger: 'change'}
					],
					version: [
						{required: true, validator: validateVersion, trigger: 'change'}
					],
					resourceUrl: [
						{required: true, message: '请在上方上传 RN 包', trigger: 'blur'},
						{required: true, validator: validateInput, trigger: 'blur'}
					],
					rnUpdateLog: [
						{required: true, message: '请输入更新日志', trigger: 'blur'},
						{required: true, validator: validateInput, trigger: 'blur'}
					]
				},
				versionList: [],
				iosVersionList: [],
				androidVersionList: [],
				fileItem: {
					name: '',
					progress: 0,
					guid: getGUID() // 加入 guid 防止多线程干扰
				}
			};
		},
		filters: {
			deviceFilter(type) {
				return type === 1 ? 'Android' : 'iOS';
			}
		},
		created() {
			if (this.rnId > 0) {
				this.isEdit = true;
				this.getPackage(this.rnId);
			} else {
				this.getVersions();
				this.inLoading = false;
			}
		},
		methods: {
			async getVersions() {
				let iosData = await http.get('/ios/versions');
				let androidData = await http.get('/android/versions');
				this.iosVersionList = iosData.data.data;
				this.androidVersionList = androidData.data.data;
				this.versionList = this.rnType === 1 ? this.androidVersionList : this.iosVersionList;
			},
			async postPackage() {
				let query = {
					rnName: this.editForm.rnName,
					rnNickName: this.editForm.rnNickName,
					rnType: this.editForm.rnType,
					resourceUrl: this.editForm.resourceUrl,
					rnSize: this.editForm.rnSize,
					rnVersion: this.editForm.rnVersion,
					rnUpdateLog: this.editForm.rnUpdateLog,
					rnStatus: this.editForm.rnStatus,
					versionMin: this.editForm.versionMin,
					versionMax: this.editForm.versionMax
				};
				let response = await http.post('/package', query);
				return response.data;
			},
			async putPackage() {
				let query = {
					rnName: this.editForm.rnName,
					rnNickName: this.editForm.rnNickName,
					rnType: this.editForm.rnType,
					resourceUrl: this.editForm.resourceUrl,
					rnSize: this.editForm.rnSize,
					rnVersion: this.editForm.rnVersion,
					rnUpdateLog: this.editForm.rnUpdateLog,
					rnStatus: this.editForm.rnStatus,
					versionMin: this.editForm.versionMin,
					versionMax: this.editForm.versionMax
				};
				let response = await http.put('/package/' + this.rnId, query);
				return response.data;
			},
			async getPackage() {
				await this.getVersions();
				let response = await http.get('package/' + this.rnId);
				if (response.data.code !== 200) {
					this.$Notice.error({
						title: '错误',
						desc: response.message
					});
					this.$emit('close-edit-modal');
				} else {
					let data = response.data.data;

					this.editForm = {
						rnName: data.rnName,
						rnNickName: data.rnNickName,
						rnType: data.rnType,
						resourceUrl: data.resourceUrl,
						rnSize: data.rnSize,
						rnVersion: data.rnVersion,
						rnUpdateLog: data.rnUpdateLog,
						rnStatus: data.rnStatus,
						versionMin: data.versionMin,
						versionMax: data.versionMax
					};

					this.$nextTick(() => {
						this.$set(this.editForm, 'versionMin', data.versionMin);
						this.$set(this.editForm, 'versionMax', data.versionMax);
						this.inLoading = false;
					});
				}
			},
			handleUpload(file) {
				if (this.editForm.rnVersion === '') {
					this.$Notice.warning({
						title: '错误',
						desc: '请先设置 RN 包版本号再上传'
					});
					return false;
				}
				let fileData = getFileName(file.name);
				this.fileItem.name = `${this.appId}-${this.editForm.rnName}-${fileData.name}-${this.editForm.rnVersion}.${fileData.ext}`;
				let guid = this.fileItem.guid;
				uploadFileToOSS(file, this.fileItem.name, response => {
					if (guid !== this.fileItem.guid) return false;
					this.editForm.resourceUrl = response.res.requestUrls[0];
					this.editForm.rnSize = file.size / 1024;
					this.fileItem = {
						name: '',
						progress: 0,
						guid: getGUID()
					};

					// 手动触发事件隐藏未填报错
					// 可能影响用户体验，因为在用户上传同时如果正在编辑更新日志，可能导致输入焦点切换让输入终止造成不良体验。

					// setTimeout(() => {
					//     this.$refs.editFormRule.$children.find(item => {
					//         if (item.prop === 'resourceUrl') {
					//             console.log(item.$el.getElementsByTagName('input')[0]);
					//             item.$el.getElementsByTagName('input')[0].focus();
					//             item.$el.getElementsByTagName('input')[0].blur();
					//         }
					//     });
					// }, 0);
				}, progress => {
					if (guid !== this.fileItem.guid) return false;
					this.$set(this.fileItem, 'progress', parseInt(progress));
				});

				// 不使用组件自身上传功能
				return false;
			},
			stopUploadFile() {
				this.fileItem = {
					name: '',
					progress: 0,
					guid: getGUID()
				};
			},
			handleSubmit(name) {
				this.$refs[name].validate((valid) => {
					if (!valid) {
						this.$Message.error('请先完成所有必填项内容!');
						return false;
					}

					if (!this.isEdit) {
						this.postPackage().then(response => {
							if (response.code !== 200) {
								this.$Notice.error({
									title: '错误',
									desc: response.message
								});
							} else {
								this.$Message.success('提交成功!');
								this.$emit('close-edit-modal');
							}
						});
					} else {
						this.putPackage().then(response => {
							if (response.code !== 200) {
								this.$Notice.error({
									title: '错误',
									desc: response.message
								});
							} else {
								this.$Message.success('更新成功!');
								this.$emit('close-edit-modal');
							}
						});
					}
				});
			},
			versionFilter(value, option) {
				return option.toUpperCase().indexOf(value.toUpperCase()) !== -1;
			},
			handleReset(name) {
				this.$refs[name].resetFields();
				this.$emit('close-edit-modal');
			},
			fmtFileSize(a) {
				return fmtFileSize(a);
			}
		},
		watch: {
			'editForm.rnType'(type) {
				this.editForm.versionMin = '';
				this.editForm.versionMax = '';
				this.versionList = type === 1 ? this.androidVersionList : this.iosVersionList;
			},
			'editForm.versionMin'() {
				this.$refs.editFormRule.validateField('version');
			},
			'editForm.versionMax'() {
				this.$refs.editFormRule.validateField('version');
			}
		}
	};
</script>
<style lang="scss">
    #rn-edit {
        .spin-icon-load {
            animation: ani-demo-spin 1s linear infinite;
        }

        @keyframes ani-demo-spin {
            from {
                transform: rotate(0deg);
            }
            50% {
                transform: rotate(180deg);
            }
            to {
                transform: rotate(360deg);
            }
        }
        .ivu-auto-complete {
            .ivu-select-dropdown {
                max-height: 200px;
            }
        }
    }
</style>
<style scoped lang="scss">
    #rn-edit {
        .footer {
            text-align: right;
            border-top: 1px solid #e9eaec;
            padding: 14px 0 0 0;
            line-height: 1;
        }
    }
</style>
