package com.captain.wiki.controller;


import com.captain.wiki.req.EbookQueryReq;
import com.captain.wiki.req.EbookSaveReq;
import com.captain.wiki.resp.CommonResp;
import com.captain.wiki.resp.EbookQueryResp;
import com.captain.wiki.resp.PageResp;
import com.captain.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    /**
     *
     * requestBody是json方面的提交
     * form表单提交就可以不用加这个注释，但是建议加上
     * respondBody是一一对
     */
    @RequestMapping("/list")
    public CommonResp list(@RequestBody EbookQueryReq req){
        //在controller不要见到domain类
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(EbookSaveReq req){
        //在controller不要见到domain类
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
}
