import uploadFileToOSS from '@/libs/oss';
import uploadFileToCOS from '@/libs/cos';

const uploadFileToCloud = (file, fileName = '', callBack, progressCB = null) => {
    switch (process.env.VUE_APP_CLOUD_STORAGE_TYPE_ENABLE) {
        case process.env.VUE_APP_OSS_CLOUD_STORAGE_TYPE:
            uploadFileToOSS(file, fileName, callBack, progressCB);
            break;
        case process.env.VUE_APP_COS_CLOUD_STORAGE_TYPE:
            uploadFileToCOS(file, fileName, callBack, progressCB);
            break;
    }
};

export default uploadFileToCloud;
