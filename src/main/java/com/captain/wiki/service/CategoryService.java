package com.captain.wiki.service;


import com.captain.wiki.domain.Category;
import com.captain.wiki.domain.CategoryExample;
import com.captain.wiki.mapper.CategoryMapper;
import com.captain.wiki.req.CategoryQueryReq;
import com.captain.wiki.req.CategorySaveReq;
import com.captain.wiki.resp.CategoryQueryResp;
import com.captain.wiki.resp.PageResp;
import com.captain.wiki.resp.SnowFlake;
import com.captain.wiki.utils.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;
    
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        //只对下面第一个遇到的sql有效
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());



        PageResp<CategoryQueryResp> pageResp = new PageResp();

        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;

    }

    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req,Category.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            //id的写法:uuid,简单的自增以及雪花算法
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else{
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }

    }

    public void delete(Long id) {
        //按照主键删除
        categoryMapper.deleteByPrimaryKey(id);
    }
}
