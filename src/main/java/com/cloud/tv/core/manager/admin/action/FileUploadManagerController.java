package com.cloud.tv.core.manager.admin.action;

import com.cloud.tv.entity.Accessory;
import com.cloud.tv.core.service.IAccessoryService;
import com.cloud.tv.core.service.ISysConfigService;
import com.cloud.tv.core.utils.ResponseUtil;
import com.cloud.tv.entity.SysConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api("文件管理")
@RestController
@RequestMapping("/admin/file")
public class FileUploadManagerController {

    @Autowired
    private IAccessoryService accessoryService;
    @Autowired
    private ISysConfigService configService;

    @ApiOperation("文件上传")
    @RequestMapping("/upload")
    public Object upload(@RequestParam(value = "file", required = false) MultipartFile file){
        Accessory accessory = this.upload(file, 0);
        if(accessory != null){
            Map map =  new HashMap();
            map.put("id", accessory.getId());
            return ResponseUtil.ok(map);
        }
        return ResponseUtil.badArgument("上传失败");
    }

    public Accessory upload(@RequestParam(required = false) MultipartFile file, int type){
        SysConfig configs = this.configService.findSysConfigList();
        String uploaFilePath = configs.getUploadFilePath();
        String path = configs.getVideoFilePath();
        if(type == 0){
            path = configs.getPhotoFilePath();
            uploaFilePath = uploaFilePath + File.separator + "photo";
        }else{
            uploaFilePath = uploaFilePath + File.separator + "vedio";
        }
        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String picNewName = fileName + ext;
        String imgRealPath = path + File.separator + picNewName;

        java.io.File imageFile = new File(imgRealPath);
        if (!imageFile.getParentFile().exists() && !imageFile.getParentFile().isDirectory()) {
            imageFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(imageFile);
            Accessory accessory = new Accessory();
            accessory.setA_name(picNewName);
            accessory.setA_path(uploaFilePath);
            accessory.setA_ext(ext);
            accessory.setA_size((int)file.getSize());
            accessory.setType(type);
            this.accessoryService.save(accessory);
            return accessory;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
