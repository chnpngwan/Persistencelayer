package org.example.mapper;


import org.example.pojo.Brand;

import java.util.List;

/**
 * ProjectName:   MyBatis
 * PackageName:   org.example.mapper
 * ClassName:     UserMapper
 * Description:  mybatis
 *
 * @Author: ChnpngWng
 * @Date: 2022/10/9 11:55
 */
public interface BrandMapper {
//    查询所有
    public List<Brand> selectAll();
}
