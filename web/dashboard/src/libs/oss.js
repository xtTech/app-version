import co from 'co';
import OSS from 'ali-oss';

const client = new OSS({
    region: process.env.VUE_APP_OSS_REGION,
    accessKeyId: process.env.VUE_APP_OSS_KEYID,
    accessKeySecret: process.env.VUE_APP_OSS_KEYSECRET,
    bucket: process.env.VUE_APP_OSS_BUCKET
});

const uploadFileToOSS = (file, fileName = '', callBack, progressCB = null) => {
    co(function * () {
        let name = fileName === '' ? file.name : fileName;
        yield client.multipartUpload(name, file, {
            progress: function * (percentage) {
                if (progressCB !== null) { progressCB(percentage * 100); }
            }
        });
        // 上传完成
        let response = yield client.head(name);
        callBack(response);
    }).catch(() => {
    });
};


const uploadFileToServer = (file, fileName = '', callBack, progressCB = null) => {
    co(function * () {
        let name = fileName === '' ? file.name : fileName;
        // yield client.multipartUpload(name, file, {
        //     progress: function * (percentage) {
        //         if (progressCB !== null) { progressCB(percentage * 100); }
        //     }
        // });
		
		/****
		 *@description:上传至本地服务器
		 ****/
		 let formData = new FormData();
		 formData.append('file',file.file);
		 formData.append('channelCode',file.channelCode);
		 let config = {
		    headers: {
		         'Content-Type' : 'content-type=multipart/form-data'
		    },
			timeout: 0,
			// `onUploadProgress` 允许为上传处理进度事件
		    onUploadProgress: function (progressEvent) {
			    // 对原生进度事件的处理
				log.info("progressEvent:"+progressEvent);
				//if (progressCB !== null) { progressCB(percentage * 100); }
		    }
		 };
		 let me=this;
		 let returnResponse =null;
		 http.post('apk/upload', formData, config).then(function (response) {
			 returnResponse=response;
		 });		 
         //上传完成
         callBack(returnResponse);
    }).catch(() => {
    });
};


export default uploadFileToOSS;
