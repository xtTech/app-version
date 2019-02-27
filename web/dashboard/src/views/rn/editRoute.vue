<template>
    <div id="rn-edit">
        <Form ref="editFormRule" :model="editForm" :rules="editFormRule" :label-width="140" style="position: relative">
            <FormItem label="适用终端" prop="device">
                <Checkbox v-model="editForm.iosEnabled" style="margin-right: 30px"> iOS</Checkbox>
                <Checkbox v-model="editForm.androidEnabled"> Android</Checkbox>
            </FormItem>
            <FormItem label="通用昵称" prop="routeName">
                <Input v-model="editForm.routeName" placeholder=""/>
            </FormItem>
            <FormItem label="被拦截 URL" prop="routeKey">
                <Input v-model="editForm.routeKey" placeholder=""/>
            </FormItem>
            <FormItem label="目标 URL" prop="routeValue">
                <Input v-model="editForm.routeValue" placeholder=""/>
            </FormItem>
            <FormItem label="版本区间" prop="iosVersion">
                <Row>
                    <Col span="10">
                        <AutoComplete
                                v-model="editForm.iosMin"
                                :clearable="editForm.iosEnabled"
                                :disabled="!editForm.iosEnabled"
                                :data="iosVersionList"
                                :filter-method="versionFilter"
                                placeholder="输入或选择版本">
                        </AutoComplete>
                    </Col>
                    <Col span="4" style="text-align: center">≤ iOS ＜</Col>
                    <Col span="10">
                        <AutoComplete
                                v-model="editForm.iosMax"
                                :clearable="editForm.iosEnabled"
                                :disabled="!editForm.iosEnabled"
                                :data="iosVersionList"
                                :filter-method="versionFilter"
                                placeholder="输入或选择版本">
                        </AutoComplete>
                        <!--<Select v-model="editForm.iosMax"-->
                                <!--:clearable="editForm.iosEnabled"-->
                                <!--:disabled="!editForm.iosEnabled">-->
                            <!--<Option v-for="verison in iosVersionList" :value="verison" :key="verison">{{verison}}-->
                            <!--</Option>-->
                        <!--</Select>-->
                    </Col>
                </Row>
            </FormItem>
            <FormItem prop="androidVersion">
                <Row>
                    <Col span="10">
                        <AutoComplete
                                v-model="editForm.androidMin"
                                :clearable="editForm.androidEnabled"
                                :disabled="!editForm.androidEnabled"
                                :data="androidVersionList"
                                :filter-method="versionFilter"
                                placeholder="输入或选择版本">
                        </AutoComplete>
                        <!--<Select v-model="editForm.androidMin"-->
                                <!--:clearable="editForm.androidEnabled"-->
                                <!--:disabled="!editForm.androidEnabled">-->
                            <!--<Option v-for="verison in androidVersionList" :value="verison" :key="verison">{{verison}}-->
                            <!--</Option>-->
                        <!--</Select>-->
                    </Col>
                    <Col span="4" style="text-align: center">≤ Android ＜</Col>
                    <Col span="10">
                        <AutoComplete
                                v-model="editForm.androidMax"
                                :clearable="editForm.androidEnabled"
                                :disabled="!editForm.androidEnabled"
                                :data="androidVersionList"
                                :filter-method="versionFilter"
                                placeholder="输入或选择版本">
                        </AutoComplete>
                        <!--<Select v-model="editForm.androidMax"-->
                                <!--:clearable="editForm.androidEnabled"-->
                                <!--:disabled="!editForm.androidEnabled">-->
                            <!--<Option v-for="verison in androidVersionList" :value="verison" :key="verison">{{verison}}-->
                            <!--</Option>-->
                        <!--</Select>-->
                    </Col>
                </Row>
            </FormItem>
            <FormItem label="状态">
                <RadioGroup v-model="editForm.routeStatus">
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
import { http, hasValue } from '@/libs/util';

export default {
    name: 'rn-edit',
    props: {
        'rnId': {
            type: Number
        }
    },
    data () {
        const validateInput = (rule, value, callback) => {
            if (value !== value.trim()) {
                callback(new Error('请移除前后空格'));
            } else {
                callback();
            }
        };
        const validateDevice = (rule, value, callback) => {
            if (!this.editForm.iosEnabled && !this.editForm.androidEnabled) {
                callback(new Error('请至少选择一个适用终端'));
            } else {
                callback();
            }
        };
        const validateIOSVersion = (rule, value, callback) => {
            if (!this.editForm.iosEnabled) {
                callback();
            } else if (!hasValue(this.editForm.iosMin) || !hasValue(this.editForm.iosMax)) {
                callback(new Error('请选择 iOS 设备适用版本'));
            } else if (this.editForm.iosMin !== this.editForm.iosMin.trim() || this.editForm.iosMax !== this.editForm.iosMax.trim()) {
                callback(new Error('请移除前后空格'));
            } else if (this.editForm.iosMin.length > 32 || this.editForm.iosMax.length > 32) {
                callback(new Error('过长的版本号'));
            } else {
                callback();
            }
        };
        const validateAndroidVersion = (rule, value, callback) => {
            if (!this.editForm.androidEnabled) {
                callback();
            } else if (!hasValue(this.editForm.androidMin) || !hasValue(this.editForm.androidMax)) {
                callback(new Error('请选择 Android 设备适用版本'));
            } else if (this.editForm.androidMin !== this.editForm.androidMin.trim() || this.editForm.androidMax !== this.editForm.androidMax.trim()) {
                callback(new Error('请移除前后空格'));
            } else if (this.editForm.androidMin.length > 32 || this.editForm.androidMax.length > 32) {
                callback(new Error('过长的版本号'));
            } else {
                callback();
            }
        };
        return {
            inLoading: true,
            isEdit: false, // 是否为编辑状态，false 表示创建，true 编辑中
            editForm: {
                iosEnabled: false,
                androidEnabled: false,
                routeName: '',
                routeKey: '',
                routeValue: '',
                iosMin: '',
                iosMax: '',
                androidMin: '',
                androidMax: '',
                routeStatus: 0
            },
            editFormRule: {
                device: [
                    { required: true, validator: validateDevice, trigger: 'change' }
                ],
                iosVersion: [
                    { required: true, validator: validateIOSVersion, trigger: 'change' }
                ],
                androidVersion: [
                    { required: true, validator: validateAndroidVersion, trigger: 'change' }
                ],
                routeName: [
                    { required: true, message: '请输入通用昵称', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ],
                routeKey: [
                    { required: true, message: '请填写被拦截 URL', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ],
                routeValue: [
                    { required: true, message: '请填写目标 URL', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ]
            },
            iosVersionList: [],
            androidVersionList: []
        };
    },
    created () {
        if (this.rnId > 0) {
            this.isEdit = true;
            this.getRoute();
        } else {
            this.getVersions();
            this.inLoading = false;
        }
    },
    methods: {
        async getVersions () {
            let iosData = await http.get('/ios/versions');
            let androidData = await http.get('/android/versions');
            this.iosVersionList = iosData.data.data;
            this.androidVersionList = androidData.data.data;
        },
        async postRoute () {
            let query = {
                iosEnabled: hasValue(this.editForm.iosEnabled) ? 1 : 0,
                androidEnabled: hasValue(this.editForm.androidEnabled) ? 1 : 0,
                routeName: this.editForm.routeName,
                routeKey: this.editForm.routeKey,
                routeValue: this.editForm.routeValue,
                iosMin: hasValue(this.editForm.iosMin) ? this.editForm.iosMin : null,
                iosMax: hasValue(this.editForm.iosMax) ? this.editForm.iosMax : null,
                androidMin: hasValue(this.editForm.androidMin) ? this.editForm.androidMin : null,
                androidMax: hasValue(this.editForm.androidMax) ? this.editForm.androidMax : null,
                routeStatus: this.editForm.routeStatus
            };
            let response = await http.post('/route', query);
            return response.data;
        },
        async putRoute () {
            let query = {
                iosEnabled: hasValue(this.editForm.iosEnabled) ? 1 : 0,
                androidEnabled: hasValue(this.editForm.androidEnabled) ? 1 : 0,
                routeName: this.editForm.routeName,
                routeKey: this.editForm.routeKey,
                routeValue: this.editForm.routeValue,
                iosMin: hasValue(this.editForm.iosMin) ? this.editForm.iosMin : null,
                iosMax: hasValue(this.editForm.iosMax) ? this.editForm.iosMax : null,
                androidMin: hasValue(this.editForm.androidMin) ? this.editForm.androidMin : null,
                androidMax: hasValue(this.editForm.androidMax) ? this.editForm.androidMax : null,
                routeStatus: this.editForm.routeStatus
            };
            let response = await http.put('/route/' + this.rnId, query);
            return response.data;
        },
        async getRoute () {
            await this.getVersions();
            let response = await http.get('route/' + this.rnId);
            if (response.data.code !== 200) {
                this.$Notice.error({
                    title: '错误',
                    desc: response.message
                });
                this.$emit('close-edit-modal');
            } else {
                let data = response.data.data;
                this.editForm = {
                    iosEnabled: data.iosEnabled === 1,
                    androidEnabled: data.androidEnabled === 1,
                    routeName: data.routeName,
                    routeKey: data.routeKey,
                    routeValue: data.routeValue,
                    iosMin: data.iosMin,
                    iosMax: data.iosMax,
                    androidMin: data.androidMin,
                    androidMax: data.androidMax,
                    routeStatus: data.routeStatus
                };

                this.$nextTick(() => {
                    this.$set(this.editForm, 'iosMin', data.iosMin);
                    this.$set(this.editForm, 'iosMax', data.iosMax);
                    this.$set(this.editForm, 'androidMin', data.androidMin);
                    this.$set(this.editForm, 'androidMax', data.androidMax);
                    this.inLoading = false;
                });
            }
        },
        handleSubmit (name) {
            this.$refs[name].validate((valid) => {
                if (!valid) {
                    this.$Message.error('请先完成所有必填项内容!');
                    return false;
                }

                if (!this.isEdit) {
                    this.postRoute().then(response => {
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
                    this.putRoute().then(response => {
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
        handleReset (name) {
            this.$refs[name].resetFields();
            this.$emit('close-edit-modal');
        },
        versionFilter (value, option) {
            return option.toUpperCase().indexOf(value.toUpperCase()) !== -1;
        }
    },
    watch: {
        'editForm.iosEnabled' (val) {
            this.editForm.iosMin = '';
            this.editForm.iosMax = '';
        },
        'editForm.androidEnabled' (val) {
            this.editForm.androidMin = '';
            this.editForm.androidMax = '';
        },
        'editForm.iosMin' (val) {
        	this.$refs.editFormRule.validateField('iosVersion');
        },
        'editForm.iosMax' (val) {
            this.$refs.editFormRule.validateField('iosVersion');
        },
        'editForm.androidMin' (val) {
        	this.$refs.editFormRule.validateField('androidVersion');
        },
        'editForm.androidMax' (val) {
            this.$refs.editFormRule.validateField('androidVersion');
        }
    }
};
</script>
<style lang="scss">
    #rn-edit {
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
