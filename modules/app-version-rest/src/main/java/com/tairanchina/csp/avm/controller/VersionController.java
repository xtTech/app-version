package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.Apk;
import com.tairanchina.csp.avm.entity.Channel;
import com.tairanchina.csp.avm.service.AndroidVersionService;
import com.tairanchina.csp.avm.service.ChannelService;
import com.tairanchina.csp.avm.service.IosVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by hzlizx on 2018/6/14 0014
 */
@Api(tags= "版本相关接口")
@RestController
@RequestMapping("/app/api/appVersion/versionController")
@PropertySource("classpath:application.yml")
public class VersionController {
    private static final Logger logger = LoggerFactory.getLogger(VersionController.class);

    @Autowired
    private AndroidVersionService androidVersionService;

    @Autowired
    private IosVersionService iosVersionService;

    @Value("${spring.profiles.active}")
    private String active;

    @Value("${rest.base.url}")
    private String baseUrl;

    /**
     * 查询新版本
     *
     * @param tenantAppId 应用ID
     * @param version     应用当前版本号
     * @param channelCode 渠道号
     * @param platform    平台（ios/android）
     * @return
     */
    @ApiOperation(value = "查询新版本", notes = "根据当前应用的版本号，渠道号，平台获取当前应用的最新版本")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantAppId", value = "应用appId", dataType = "string", defaultValue = "uc28ec7f8870a6e785", required = true),
            @ApiImplicitParam(name = "version", value = "版本号", required = true),
            @ApiImplicitParam(name = "channelCode", value = "渠道码，官方渠道的渠道码为official", required = true),
            @ApiImplicitParam(name = "platform", value = "平台，值应为 ios 或 android", required = true),
    })
    @GetMapping("/{tenantAppId}-{version}-{channelCode}-{platform}")
    public ServiceResult version(@PathVariable String tenantAppId,
                                 @PathVariable String version,
                                 @PathVariable String channelCode,
                                 @PathVariable String platform) {
        logger.info("appId={},version={},channelCode={},platform={}", tenantAppId, version, channelCode, platform);
        if ("android".equalsIgnoreCase(platform)) {
            ServiceResult serviceResult = androidVersionService.findNewestVersion(tenantAppId, version, channelCode);
            if (serviceResult.getCode() == 200) {
                HashMap<String, Object> data = (HashMap<String, Object>) serviceResult.getRecord();
                String downloadUrl = (String) data.get("downloadUrl");
                String downloadApkUrl = (String) data.get("downloadApkUrl");
//              if("prod".equals(active)){
//                 downloadUrl = baseUrl + downloadUrl;
//              }else {
//                 String protocol = request.getScheme();
//                 if(request.getServerPort()==80){
//                    downloadUrl = protocol.toLowerCase()+"://"+request.getServerName()+downloadUrl;
//                 }else {
//                    downloadUrl = protocol.toLowerCase()+"://"+request.getServerName()+":"+request.getServerPort()+downloadUrl;
//                 }
//              }
                data.put("downloadUrl", baseUrl + downloadUrl);
                data.put("downloadApkUrl", baseUrl + downloadApkUrl);
                return ServiceResult.ok(data);
            }
            return androidVersionService.findNewestVersion(tenantAppId, version, channelCode);
        } else if ("ios".equalsIgnoreCase(platform)) {
            return iosVersionService.findNewestVersion(tenantAppId, version);
        } else {
            return ServiceResultConstants.PLATFORM_ERROR;
        }
    }

    /**
     * 下载某个版本
     * @param apkId APK的ID
     * @return 跳转下载
     */
    @ApiOperation(value = "下载APK，根据APK包ID", notes = "根据给定的APK_ID下载相应的APK包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apkId", value = "APK包的ID", required = true),
    })
    @GetMapping("/download/{apkId}")
    public ServiceResult download(@PathVariable int apkId,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException {
        logger.info("下载了APK[{}]", apkId);
        if (apkId < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        } else {
            ServiceResult serviceResult = androidVersionService.getDownloadUrl(apkId);
            if (serviceResult.getCode() != 200) {
                return serviceResult;
            } else {
                //跳转下载
                Apk apk = (Apk) serviceResult.getRecord();
//              HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//              String downloadUrl=baseUrl+apk.getOssUrl();
//              response.sendRedirect(downloadUrl);
                ServiceResult serviceResult1=downloadFile(apk.getAppId(),apk.getChannelId(),apk.getApkName(),httpServletRequest,httpServletResponse);
                return serviceResult1;
            }
        }
    }

    @Autowired
    private ChannelService channelService;

    @Value("${is_use_project_path}")
    private boolean is_use_project_path;
    @Value("${uploadFilePath}")
    private String uploadFilePath;
    @Value("${downloadFileUrl}")
    private String downloadFileUrl;

    private ServiceResult downloadFile(Integer appId,Integer channelId,String fileName,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
        //如果文件名不为空，则进行下载
        if(!StringUtils.isEmpty(fileName)){
            Channel channel=this.channelService.findChannelByChannelId(appId,channelId);
            String disk_uploadFilePath=channel.getUploadFolder();
            System.out.print("fileName="+fileName);
            String downloadFileName=fileName;
            String downloadFilePath= null;
            try {
                if(is_use_project_path){ //使用项目中编译目录文件夹
                    downloadFilePath = ResourceUtils.getURL("classpath:").getPath() + uploadFilePath + "\\" + channel.getChannelCode()+ "\\" + downloadFileName;
                }else{
                    downloadFilePath = disk_uploadFilePath + "\\" + downloadFileName;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            File downloadFile=new File(downloadFilePath);
            //如果文件存在，则进行下载
            if(downloadFile.exists()){

                // 实现文件下载
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(downloadFile);
                    bis = new BufferedInputStream(fis);
                    byte[] buffer = new byte[bis.available()];
                    bis.read(buffer);

                    // 设置文件大小
                    // 清空response


                    httpServletResponse.reset();

                    // 配置文件下载
                    httpServletResponse.setContentType("application/octet-stream");
                    httpServletResponse.addHeader("Content-Length",String.valueOf(downloadFile.length()));
                    try{
                        // 下载文件能正常显示中文

                        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName,"UTF-8"));
                    } catch (UnsupportedEncodingException e){
                        System.out.println("Download  failed!");
                        return ServiceResult.failed();
                    }
                    OutputStream os = httpServletResponse.getOutputStream();
                    os.write(buffer);
                    os.flush();
                    os.close();
                    if (bis != null) {
                        bis.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    System.out.println("Download  successfully!");
                    return ServiceResult.ok();
                } catch (IOException e) {
                    System.out.println("Download  failed!");
                    return ServiceResult.failed();
                }
            }
        }
        return ServiceResult.failed();
    }

    /**
     * 下载最新的APK包
     *
     * @return 跳转下载
     */
    @ApiOperation(value = "直接下载应用的最新APK包", notes = "根据应用ID下载最新的APK包，默认下载官方渠道的最新APK包")
    @GetMapping("/apk/download/{appId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用的appId", defaultValue = "uc28ec7f8870a6e785", required = true),
            @ApiImplicitParam(name = "channelCode", value = "渠道码，非必填，默认为下载官方渠道的APK包", defaultValue = "official"),
    })
    public ServiceResult downloadAPK(@PathVariable String appId, @RequestParam(required = false) String channelCode) {
        return androidVersionService.findNewestApk(appId, channelCode);
    }
}
