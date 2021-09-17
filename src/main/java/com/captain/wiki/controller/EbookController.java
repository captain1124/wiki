package com.captain.wiki.controller;


import com.captain.wiki.req.EbookReq;
import com.captain.wiki.resp.CommonResp;
import com.captain.wiki.resp.EbookResp;
import com.captain.wiki.resp.PageResp;
import com.captain.wiki.service.EbookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @RequestMapping("/list")
    public CommonResp list(EbookReq req){
        //在controller不要见到domain类
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
