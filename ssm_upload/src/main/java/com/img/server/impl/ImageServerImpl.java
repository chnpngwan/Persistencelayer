package com.img.server.impl;

import com.img.entity.Image;
import com.img.mapper.ImageMapper;
import com.img.server.ImageServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  com.img.server.impl
 * ClassName:    ImageServerImpl
 *
 * @Author chnpngwng
 * @Date 2024 05 21 14 15
 **/

@Service
public class ImageServerImpl implements ImageServer {

    @Autowired
    private ImageMapper imageMapper;

    @Transactional
    @Override
    public Integer save(MultipartFile file, Image record, ModelMap map) {
        // 保存图片的路径，图片上传成功后，将路径保存到数据库
        String filePath = "F:\\upload";
        // 获取原始图片的扩展名
        String originalFilename = file.getOriginalFilename();
        // 生成文件新的名字
        String newFileName = UUID.randomUUID() + originalFilename;
        // 封装上传文件位置的全路径
        File targetFile = new File(filePath, newFileName);
        file.transferTo(targetFile);

        // 保存到数据库
        product.setPimage(newFileName);
        productDao.save(product);
        return "redirect:/listImages";
    }
}
