package com.captain.wiki.service;


import com.captain.wiki.domain.Ebook;
import com.captain.wiki.domain.EbookExample;
import com.captain.wiki.mapper.EbookMapper;
import com.captain.wiki.req.EbookReq;
import com.captain.wiki.resp.EbookResp;
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

    public PageResp<EbookResp> list(EbookReq req){
        
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
        PageResp<EbookResp> pageResp = new PageResp();

        List<EbookResp> respList = CopyUtil.copyList(ebookList,EbookResp.class);
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;

    }
}
