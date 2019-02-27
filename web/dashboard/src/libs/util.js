import ajax from './http';

import filesize from 'filesize';

import SparkMD5 from 'spark-md5';

export const http = ajax;

/**
 * 更新网站 Title
 * @param title
 */
export const updateSiteTitle = title => {
    const suffix = 'APP 版本管理平台';
    title = title ? title + ' - ' + suffix : suffix;
    window.document.title = title;
};

/**
 * 根据日期时间戳返回指定格式日期
 * @param date
 * @param fmt format 日期格式
 * @returns string
 */
export const formatDate = (date, fmt) => {
    const padLeftZero = str => {
        return ('00' + str).substr(str.length);
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    let o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds()
    };
    for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            let str = o[k] + '';
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str));
        }
    }
    return fmt;
};
/**
 * 判断是否为对象
 * @param o any
 * @returns boolean
 */
export const isObject = o => {
    let ctor, prot;
    const isObjectObject = o => {
        return (
            o != null &&
            typeof o === 'object' &&
            Array.isArray(o) === false &&
            Object.prototype.toString.call(o) === '[object Object]'
        );
    };

    if (isObjectObject(o) === false) return false;

    // If has modified constructor
    ctor = o.constructor;
    if (typeof ctor !== 'function') return false;

    // If has modified prototype
    prot = ctor.prototype;
    if (isObjectObject(prot) === false) return false;

    // If constructor does not have an Object-specific method
    if (prot.hasOwnProperty('isPrototypeOf') === false) {
        return false;
    }

    // Most likely a plain Object
    return true;
};

/**
 * 判断是否为数组
 * @param arg any
 * @returns boolean
 */
export const isArray = arg => {
    return Array.isArray(arg);
};

/**
 * 判断是否为字符串
 * @param arg any
 * @returns boolean
 */
export const isString = arg => {
    return typeof arg === 'string';
};

/**
 * 检查值是否符合为空
 * @param value 字符串
 * @returns {boolean}
 */
export const hasValue = value => {
    if (value === '') return false;

    if (value === 0) return false;

    if (value === null) return false;

    if (value === false) return false;

    if (value === '0') return false;

    if (value === undefined) return false;

    if (isArray(value)) {
        if (value.length === 0) return false;
    }

    if (isObject(value)) {
        let i = 0;
        for (let v in value) i++;
        return i > 0;
    }

    return true;
};

/**
 * 检查对象是否存在
 * @param value 字符串 xxx.xxx.xxx
 * @returns {boolean}
 */
export const hasObject = (value, root) => {
    if (!hasValue(root)) return false;

    if (!isString(value)) return false;

    let keys = value.split('.');

    let v = root;
    for (let i in keys) {
        v = v[keys[i]];
        if (!hasValue(v)) return false;
    }
    return true;
};

/**
 * 获取格式化后的包大小,切记传入 kb 尺寸
 * @param value
 * @returns {String}
 */
export const fmtFileSize = value => {
    let kbTobit = value * 1024;
    return filesize(kbTobit);
};

/**
 * 获取唯一的 GUID
 * @returns {string}
 */
export const getGUID = () => {
    const s4 = () => {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    };
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
};

/**
 * 获取文件信息（文件名，后缀）
 * @param fileanme
 * @returns {{ext: string, name: string}}
 */
export const getFileName = fileanme => {
    let index = fileanme.lastIndexOf('.');
    let ext = fileanme.substr(index + 1);
    let name = fileanme.substring(0, index);
    return {
        ext, name
    };
};

/**
 * 计算文件 MD5 值并写入 file.md5
 * @param file
 * @returns {Promise<any>}
 */
export const getFileMd5 = file => {
    return new Promise((resolve) => {
        let blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;
        let chunkSize = 2097152; // Read in chunks of 2MB
        let chunks = Math.ceil(file.size / chunkSize);
        let currentChunk = 0;
        let spark = new SparkMD5.ArrayBuffer();
        let fileReader = new FileReader();

        fileReader.onload = e => {
            // console.log('read chunk nr', currentChunk + 1, 'of', chunks);
            spark.append(e.target.result); // Append array buffer
            currentChunk++;

            if (currentChunk < chunks) {
                loadNext();
            } else {
                resolve(spark.end());
            }
        };

        const loadNext = () => {
            let start = currentChunk * chunkSize;
            let end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;

            fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
        };

        loadNext();
    });
};

export default {
    updateSiteTitle,
    formatDate,
    fmtFileSize,
    getFileMd5,
    getFileName,
    getGUID,
    isObject,
    isArray,
    isString,
    hasValue,
    hasObject,
    ajax,
    http
};
