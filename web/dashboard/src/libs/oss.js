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

export default uploadFileToOSS;
