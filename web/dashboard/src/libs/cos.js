import COS from 'cos-js-sdk-v5';

const client = new COS({
    SecretId: process.env.VUE_APP_COS_KEYID,
    SecretKey: process.env.VUE_APP_COS_KEYSECRET
});

const uploadFileToCOS = (file, fileName = '', callBack, progressCB = null) => {
    client.putObject({
        Bucket: process.env.VUE_APP_COS_BUCKET,
        /* 必须 */
        Region: process.env.VUE_APP_COS_REGION,
        /* 存储桶所在地域，必须字段 */
        Key: fileName,
        /* 必须 */
        StorageClass: 'STANDARD',
        Body: file, // 上传文件对象
        onProgress: function (progressData) {
            console.log('progress data:', progressData);
            if (progressCB !== null) {
                progressCB(progressData.percent * 100);
            }
            // console.log(JSON.stringify(progressData));
        }
    }, function (err, data) {
        console.log('err && data', err, data);
        if (data && !err) {
            callBack({
                fileCloudUrl: data.Location
            });
        }

        console.log(err || data);
    });
};

export default uploadFileToCOS;
