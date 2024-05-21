package com.img.server;

import com.img.entity.Image;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  com.img.server
 * ClassName:    ImageServer
 *
 * @Author chnpngwng
 * @Date 2024 05 21 14 15
 **/
public interface ImageServer {

    /**
     * 上传一张图片
     * @param record
     * @return
     */
    Integer save(Image record);

    Integer save(MultipartFile file, Image record, ModelMap map);
}
