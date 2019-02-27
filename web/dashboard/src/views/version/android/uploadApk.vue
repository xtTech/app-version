<template>
    <div id="version-android-apk_upload">
        <div class="uploaditem">
            <Upload action="#"
                    multiple
                    accept="application/vnd.android.package-archive"
                    :format="['apk']"
                    :before-upload="handleUpload"
                    type="drag">
                <div style="padding: 20px 0">
                    <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                    <p>轻击或拖曳 Apk 包文件至此上传</p>
                </div>
            </Upload>
        </div>
        <ul class="filelist" v-if="fileList.length !== 0">
            <li v-for="(file,index) in fileList" :key="index" v-if="!file.isDel">
                <div class="code">
                    <label>文件: </label>
                    {{file.file.name}}
                    <Tag color="blue">{{fmtFileSize(file.file.size)}}</Tag>
                </div>
                <div class="code " :class="{
                    'ivu-form-item-error':hasValue(file.channelCodeMsg)
                }">
                    <label>渠道码:</label>
                    <Select
                            v-model="file.channelCode"
                            filterable
                            remote
                            :clearable="!file.disabled"
                            :disabled="file.disabled"
                            placeholder="请输入渠道码"
                            loading-text="正在搜索相关渠道码"
                            not-found-text="没有找到和这个渠道有关的内容,请检查该渠道是否添加"
                            @on-change="resetSelect(file,index)"
                            @on-clear="resetSelect(file,index)"
                            :loading="channelsLoading"
                            :remote-method="getChannels">
                        <Option v-for="option in channelCodes" :value="option.channelCode" :key="option.channelCode + file.md5" :label="option.channelCode" />
                    </Select>
                    <Progress v-if="file.disabled" :percent="file.percent" status="active" :stroke-width="3"></Progress>
                    <p :label="file.channelCodeMsg"></p>
                </div>
                <Row style="text-align: right;margin-top: 6px">
                    <Col>
                         <template v-if="file.percent !== 100">
                             <template v-if="!file.disabled">
                                 <Button type="error" size="small" style="margin-right: 12px;" @click="file.isDel = true">删除</Button>
                                 <Button type="primary" size="small" @click="uploadApk(file,index)">开始上传</Button>
                             </template>
                             <template v-else>
                                 <Poptip
                                         style="text-align: left"
                                         confirm
                                         title="你确定要停止并删除这个任务吗?"
                                         @on-ok="stopUpload(file,index)">
                                     <Button type="error" size="small" style="margin-right: 12px;">停止上传</Button>
                                 </Poptip>
                                 <Button disabled size="small">正在上传</Button>
                             </template>
                         </template>
                        <template v-else>
                            <Button type="success" size="small">上传成功</Button>
                        </template>
                    </Col>
                </Row>
            </li>
        </ul>
    </div>
</template>
<script>
import {
    getFileMd5,
    getFileName,
    fmtFileSize,
    http,
    getGUID,
    hasValue
} from '@/libs/util';
import uploadFileToOSS from '@/libs/oss';
export default {
    name: 'upload-apk',
    props: {
        androidId: {
            type: Number
        },
        appVersion: {
            type: String
        },
        appName: {
            type: String
        }
    },
    data () {
        return {
            channelCodes: [],
            channelsLoading: false,
            fileList: []
        };
    },
    created () {},
    methods: {
        getChannels (channelCode) {
            if (this.channelsLoading) {
                return false;
            }

            this.channelsLoading = true;
            // setTimeout(() => {
            http
                .get('/channel', {
                    params: {
                        page: 1,
                        pageSize: 20,
                        channelName: null,
                        channelStatus: 1,
                        channelCode
                    }
                })
                .then(response => {
                    if (response.data.code === 200) {
                        this.channelCodes = response.data.data.records;
                    } else {
                        this.$Notice.error({
                            title: '请求失败',
                            desc: response.data.message
                        });
                    }
                    this.channelsLoading = false;
                });
            // }, 500); // 给予适当的输入延时，降低服务器查询压力
        },
        async getExistsApk (channelCode) {
            let response = await http.get('/apk/exists', {
                params: {
                    channelCode,
                    versionId: this.androidId
                }
            });
            if (response.data.data.exists) {
                this.$Notice.error({
                    title: '请求失败',
                    desc: response.data.message
                });
            }
            return response.data;
        },
        async postApk (file, ossUrl) {
            // guid 如果不匹配则停止上传任务
            // 这里无法终止阿里云 OSS 的上传进程，所以在接口这里做限制
            if (file.guid !== file.constGUID) return false;

            let response = await http.post('/apk', {
                fileName: file.name,
                channel: file.channelCode,
                versionId: this.androidId,
                md5: file.md5,
                ossUrl
            });

            if (response.data.code === 200) {
                file.percent = 100;

                // 传递消息给列表，请求刷新列表
                this.$emit('on-upload-success');

                // 延时 5 秒移除这个项目在列表 ?
                // setTimeout(() => {
                //
                // }, 1000 * 5);
            } else {
                this.stopUpload(file);
                this.$Notice.error({
                    title: `上传${file.file.name}失败`,
                    desc: `${response.data.message}`
                });
            }
        },
        async uploadApk (file, index) {
            if (!hasValue(file.channelCode)) {
                file.channelCodeMsg = '请选择一个渠道码';
                return false;
            }

            /**
			 * 检查渠道是否存在
			 */
            let response = await this.getExistsApk(file.channelCode);
            if (response.code !== 200 || response.data.exists !== false) {
                file.channelCodeMsg = response.message;
                return false;
            }

            /**
			 * 冻结,防止误操作
			 */
            file.channelCodeMsg = '';
            file.disabled = true;

            /**
			 * 拼接文件名称
			 */
            file.name = `${this.appName}-${file.channelCode}-${this.appVersion}.${
                file.ext
            }`;

            /**
			 * 上传至OSS
			 */
            uploadFileToOSS(
                file.file,
                file.name,
                response => {
                    /**
					 * 创建 Apk
					 */
                    this.postApk(file, response.res.requestUrls[0]);
                },
                progress => {
                    file.percent = parseInt(progress) - 1 < 0 ? 0 : parseInt(progress) - 1;
                }
            );
        },
        stopUpload (file, index) {
            file.disabled = false;
            file.percent = 0;
            // 改变 GUID 值以在上传时候判断时终止它
            file.guid = getGUID();

            // 移除在列表
            file.isDel = true;
        },
        handleUpload (file) {
            /**
			 * 构建文件
			 */
            let guid = getGUID();
            getFileMd5(file).then(md5 => {
                let fileData = {
                    file,
                    md5,
                    guid, // 可随着项目改变的 GUID
                    constGUID: guid, // 固定不变的 GUID
                    disabled: false,
                    percent: 0,
                    channelCode: '', // 渠道码
                    channelCodeMsg: '',
                    isDel: false, // 是否被删除
                    name: '',
                    ext: getFileName(file.name).ext.toLowerCase()
                };

                if (fileData.ext === 'apk') {
                    this.fileList.push(fileData);
                }
            });

            return false;
        },
        resetSelect (file, index) {
            file.channelCodeMsg = '';
        },
        fmtFileSize (s) {
            return fmtFileSize(s / 1024);
        },
        hasValue (v) {
            return hasValue(v);
        }
    },
    watch: {}
};
</script>
<style lang="scss">
#version-android-apk_upload {
	.uploaditem {
		margin-bottom: 0;
	}
	.ivu-upload-list {
		display: none;
	}
	.filelist {
		list-style: none;
		.ivu-tag {
			margin-left: 6px;
			cursor: default;
		}
		.ivu-form-item {
			margin-bottom: 0;
		}
		& > li {
			background-color: #fbfbfb;
			padding: 12px;
			margin: 12px 0;
			list-style: none;
			position: relative;
		}
		.code {
			margin-top: 12px;
			display: flex;
			align-items: center;
			label {
				width: 60px;
			}
			.ivu-select {
				display: block;
			}
			.ivu-progress {
				position: absolute;
				margin-left: 57px;
				margin-top: 13px;
				width: calc(100% - 27px);
				.ivu-progress-text-inner {
					position: absolute;
					margin-top: -23px;
					margin-left: -40px;
				}
				.ivu-progress-bg {
					border-radius: 0 0 0 3px;
				}
				.ivu-icon {
					margin-left: 12px;
				}
			}
			& > p {
				position: absolute;
				margin-top: 30px;
				margin-left: 56px;
				color: #ed3f14;
			}
		}
	}
}
</style>
<style scoped lang="scss">
#version-android-apk_upload {
}
</style>
