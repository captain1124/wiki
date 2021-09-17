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
    public CommonResp save(@RequestBody EbookSaveReq req){
        //在controller不要见到domain类
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        //无响应的
        return resp;
    }


    // @RequestParam和@PathVariable都能够完成类似的功能——因为本质上，它们都是用户的输入，只不过输入的部分不同，一个在URL路径部分，另一个在参数部分。
    //通过@PathVariable，例如/blogs/1
    //通过@RequestParam，例如blogs?blogId=1

    //改成了delete的mapping
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        //在controller不要见到domain类
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        //无响应的
        return resp;
    }


}
