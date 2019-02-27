<template>
    <div id="capi-edit">
        <Form ref="editFormRule" :model="editForm" :rules="editFormRule" :label-width="80" style="position: relative">
            <FormItem label="适用终端" prop="device">
                <Checkbox v-model="editForm.iosEnabled" style="margin-right: 30px">iOS</Checkbox>
                <Checkbox v-model="editForm.androidEnabled">Android</Checkbox>
            </FormItem>
            <FormItem label="接口名称" prop="customName">
                <Input v-model="editForm.customName" placeholder=""/>
            </FormItem>
            <FormItem label="接口 Key" prop="customKey">
                <Input v-model="editForm.customKey" placeholder=""/>
            </FormItem>
            <FormItem label="配置内容" prop="customContent">
                <div ref="code" :class="codeError ? 'form-code-error' : ''"></div>
                <Input class="code-textarea" v-model="editForm.customContent" type="textarea"/>
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
                    </Col>
                </Row>
            </FormItem>
            <FormItem label="状态">
                <RadioGroup v-model="editForm.customStatus">
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
    name: 'capi-edit',
    props: {
        'cApiId': {
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
        const validateJson = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入配置内容'));
                this.codeError = true;
            } else if (value !== value.trim()) {
                callback(new Error('请移除前后空格'));
                this.codeError = true;
            } else {
                try {
                    JSON.parse(value);
                    callback();
                    this.codeError = false;
                } catch (e) {
                    callback(e);
                    this.codeError = true;
                }
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
                customName: '',
                customKey: '',
                customContent: '',
                iosMin: '',
                iosMax: '',
                androidMin: '',
                androidMax: '',
                customStatus: 0
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
                customName: [
                    { required: true, message: '请输入接口名称', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' }
                ],
                customKey: [
                    { required: true, message: '请输入接口 Key', trigger: 'blur' },
                    { required: true, validator: validateInput, trigger: 'blur' },
                    { required: true, pattern: /^[0-9a-zA-Z_]*$/g, message: '接口 Key 中只允许包含 字母、数字和 _（下划线）', trigger: 'blur' }
                ],
                customContent: [
                    { required: true, validator: validateJson, trigger: 'blur' }
                ]
            },
            iosVersionList: [],
            androidVersionList: [],
            codeMirror: null,
            codeError: false
        };
    },
    created () {
        if (this.cApiId > 0) {
            this.isEdit = true;
            this.getCApi(this.cApiId);
        } else {
            this.getVersions();
            this.inLoading = false;
        }
    },
    mounted () {
        this.$nextTick(() => {
            this.codeMirror = new CodeMirror(this.$refs.code, {
                value: '{\n\n}',
                lineNumbers: true,
                lineWrapping: true,
                foldGutter: true,
                readOnly: false,
                autofocus: true,
                cursorScrollMargin: 10,
                theme: 'neo',
                gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter'],
                mode: 'application/ld+json'
            });

            this.codeMirror.on('change', (el, changeObj) => {
                this.editForm.customContent = el.getValue();
                // 手动触发 form customContent 验证
                if (hasValue(this.$refs.editFormRule)) {
                    this.$refs.editFormRule.validateField('customContent');
                }
            });
        });
    },
    methods: {
        async getVersions () {
            let iosData = await http.get('/ios/versions');
            let androidData = await http.get('/android/versions');
            this.iosVersionList = iosData.data.data;
            this.androidVersionList = androidData.data.data;
        },
        async postCApi () {
            let query = {
                iosEnabled: hasValue(this.editForm.iosEnabled) ? 1 : 0,
                androidEnabled: hasValue(this.editForm.androidEnabled) ? 1 : 0,
                customName: this.editForm.customName,
                customKey: this.editForm.customKey,
                customContent: JSON.stringify(JSON.parse(this.editForm.customContent)), // 转为对象再转回来以序列化内容成一行
                iosMin: hasValue(this.editForm.iosMin) ? this.editForm.iosMin : null,
                iosMax: hasValue(this.editForm.iosMax) ? this.editForm.iosMax : null,
                androidMin: hasValue(this.editForm.androidMin) ? this.editForm.androidMin : null,
                androidMax: hasValue(this.editForm.androidMax) ? this.editForm.androidMax : null,
                customStatus: this.editForm.customStatus
            };
            let response = await http.post('/capi/add', query);
            return response.data;
        },
        async putCApi () {
            let query = {
                iosEnabled: hasValue(this.editForm.iosEnabled) ? 1 : 0,
                androidEnabled: hasValue(this.editForm.androidEnabled) ? 1 : 0,
                customName: this.editForm.customName,
                customKey: this.editForm.customKey,
                customContent: JSON.stringify(JSON.parse(this.editForm.customContent)), // 转为对象再转回来以序列化内容成一行
                iosMin: hasValue(this.editForm.iosMin) ? this.editForm.iosMin : null,
                iosMax: hasValue(this.editForm.iosMax) ? this.editForm.iosMax : null,
                androidMin: hasValue(this.editForm.androidMin) ? this.editForm.androidMin : null,
                androidMax: hasValue(this.editForm.androidMax) ? this.editForm.androidMax : null,
                customStatus: this.editForm.customStatus
            };
            let response = await http.put(`/capi/update/${this.cApiId}`, query);
            return response.data;
        },
        async getCApi () {
            await this.getVersions();
            let response = await http.get('/capi/' + this.cApiId);

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
                    customName: data.customName,
                    customKey: data.customKey,
                    customContent: data.customContent,
                    iosMin: data.iosMin,
                    iosMax: data.iosMax,
                    androidMin: data.androidMin,
                    androidMax: data.androidMax,
                    customStatus: data.customStatus
                };

                let watchIng = setInterval(() => {
                    if (this.codeMirror !== null) {
                        this.codeMirror.setValue(jsbeautifier(data.customContent));
                        clearInterval(watchIng);
                    }
                }, 100);

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
                    this.postCApi().then(response => {
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
                    this.putCApi().then(response => {
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
    #capi-edit{
        .code-textarea{
            &.ivu-input-wrapper{
                display: none;
            }
        }
        .form-code-error{
            .CodeMirror-wrap{
                border: 1px solid #ed3f14;
                border-radius: 4px;
                &.CodeMirror-focused{
                    border-color: #ed3f14;
                    outline: 0;
                    box-shadow:0 0 0 2px rgba(237,63,20,.2);
                }
            }
        }
        .ivu-auto-complete{
            .ivu-select-dropdown{
                max-height: 200px;
            }
        }
    }
</style>
<style scoped lang="scss">
    #capi-edit {
        .footer {
            text-align: right;
            border-top: 1px solid #e9eaec;
            padding: 14px 0 0 0;
            line-height: 1;
        }
    }
</style>
