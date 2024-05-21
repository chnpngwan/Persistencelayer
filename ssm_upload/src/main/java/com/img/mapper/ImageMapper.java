package com.img.mapper;

import com.img.entity.Image;
import java.util.List;

public interface ImageMapper {
    int deleteByPrimaryKey(byte[] id);

    int insert(Image record);

    Image selectByPrimaryKey(byte[] id);

    List<Image> selectAll();

    int updateByPrimaryKey(Image record);

    /**
     * 上传一张图片
     * @param record
     * @return
     */
    Integer save(Image record);
}