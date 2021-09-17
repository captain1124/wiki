package com.captain.wiki.controller;


import com.captain.wiki.req.CategoryQueryReq;
import com.captain.wiki.req.CategorySaveReq;
import com.captain.wiki.resp.CommonResp;
import com.captain.wiki.resp.CategoryQueryResp;
import com.captain.wiki.resp.PageResp;
import com.captain.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     *
     * requestBody是json方面的提交
     * form表单提交就可以不用加这个注释，但是建议加上
     * respondBody是一一对
     */
    @RequestMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req){
        //在controller不要见到domain类
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req){
        //在controller不要见到domain类
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
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
        categoryService.delete(id);
        //无响应的
        return resp;
    }


}
