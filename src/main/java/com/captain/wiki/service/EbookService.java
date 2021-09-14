package com.captain.wiki.service;


import com.captain.wiki.domain.Ebook;
import com.captain.wiki.domain.EbookExample;
import com.captain.wiki.mapper.EbookMapper;
import com.captain.wiki.req.EbookReq;
import com.captain.wiki.resp.EbookResp;
import com.captain.wiki.utils.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        //不為空才加入這個條件
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);
//        List<EbookResp> respList = new ArrayList<>();
//
//        for (Ebook ebook : ebooks) {
        //对象复制
////            EbookResp ebookResp = new EbookResp();
////            BeanUtils.copyProperties(ebook,ebookResp);
//            EbookResp ebookResp = CopyUtil.copy(ebook,EbookResp.class);
//            respList.add(ebookResp);
//        }
        List<EbookResp> respList = CopyUtil.copyList(ebooks,EbookResp.class);

        return respList;

    }
}
