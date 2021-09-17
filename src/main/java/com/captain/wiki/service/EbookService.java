package com.captain.wiki.service;


import com.captain.wiki.domain.Ebook;
import com.captain.wiki.domain.EbookExample;
import com.captain.wiki.mapper.EbookMapper;
import com.captain.wiki.req.EbookQueryReq;
import com.captain.wiki.req.EbookSaveReq;
import com.captain.wiki.resp.EbookQueryResp;
import com.captain.wiki.resp.PageResp;
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
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;
    
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public PageResp<EbookQueryResp> list(EbookQueryReq req){
        
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        //不為空才加入這個條件
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        //只对下面第一个遇到的sql有效
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


        //        List<EbookResp> respList = new ArrayList<>();
        //
        //        for (Ebook ebook : ebooks) {
                //对象复制
        ////            EbookResp ebookResp = new EbookResp();
        ////            BeanUtils.copyProperties(ebook,ebookResp);
        //            EbookResp ebookResp = CopyUtil.copy(ebook,EbookResp.class);
        //            respList.add(ebookResp);
        //        }
        PageResp<EbookQueryResp> pageResp = new PageResp();

        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;

    }

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req,Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            ebookMapper.insert(ebook);
        }else{
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }
}
