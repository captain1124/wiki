package com.captain.wiki.controller;


import com.captain.wiki.req.DocQueryReq;
import com.captain.wiki.req.DocSaveReq;
import com.captain.wiki.resp.CommonResp;
import com.captain.wiki.resp.DocQueryResp;
import com.captain.wiki.resp.PageResp;
import com.captain.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    /**
     *
     * requestBody是json方面的提交
     * form表单提交就可以不用加这个注释，但是建议加上
     * respondBody是一一对
     */
    @RequestMapping("/list")
    public CommonResp list(@Valid DocQueryReq req){
        //在controller不要见到domain类
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }


    @RequestMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable long ebookId){
        //在controller不要见到domain类
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        //在controller不要见到domain类
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        //无响应的
        return resp;
    }


    // @RequestParam和@PathVariable都能够完成类似的功能——因为本质上，它们都是用户的输入，只不过输入的部分不同，一个在URL路径部分，另一个在参数部分。
    //通过@PathVariable，例如/blogs/1
    //通过@RequestParam，例如blogs?blogId=1
    //改成了delete的mapping
    @DeleteMapping("/delete/{idStr}")
    public CommonResp delete(@PathVariable String idStr){
        //在controller不要见到domain类
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idStr.split(","));
        docService.delete(list);
        //无响应的
        return resp;
    }

    @RequestMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id){
        //在controller不要见到domain类
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }

    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id) {
        CommonResp commonResp = new CommonResp();
        docService.vote(id);
        return commonResp;
    }

}
