package com.captain.wiki.service;


import com.captain.wiki.domain.Content;
import com.captain.wiki.domain.Doc;
import com.captain.wiki.domain.DocExample;
import com.captain.wiki.exception.BusinessException;
import com.captain.wiki.exception.BusinessExceptionCode;
import com.captain.wiki.mapper.ContentMapper;
import com.captain.wiki.mapper.DocMapper;
import com.captain.wiki.mapper.DocMapperCust;
import com.captain.wiki.req.DocQueryReq;
import com.captain.wiki.req.DocSaveReq;
import com.captain.wiki.resp.DocQueryResp;
import com.captain.wiki.resp.PageResp;
import com.captain.wiki.utils.CopyUtil;
import com.captain.wiki.utils.RedisUtil;
import com.captain.wiki.utils.RequestContext;
import com.captain.wiki.utils.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {



    @Resource
    private DocMapper docMapper;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WsService wsService;

//    @Resource
//    private WebSocketServer webSocketServer;

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);



    public PageResp<DocQueryResp> list(DocQueryReq req){

        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();

        //只对下面第一个遇到的sql有效
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());



        PageResp<DocQueryResp> pageResp = new PageResp();

        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;

    }

    public List<DocQueryResp> all(Long ebookId){

        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");

        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);


        return list;

    }

    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req,Doc.class);
        Content content = CopyUtil.copy(req, Content.class);


        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            //id的写法:uuid,简单的自增以及雪花算法
            doc.setId(snowFlake.nextId());
            //底层插入的时候包括了所有的字段，导致default0失效
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            doc.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            //更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(count==0){
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id) {
        //按照主键删除
        docMapper.deleteByPrimaryKey(id);
    }

    public String findContent(Long id) {
        //按照主键删除
        Content content = contentMapper.selectByPrimaryKey(id);
        //文档阅读数+1
        docMapperCust.increaseViewCount(id);
        if(ObjectUtils.isEmpty(content)){
            return "";
        }else{
            return content.getContent();
        }
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        //按照数据删除
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }


    /**
     * 点赞
     * @param id
     */

    public void vote(Long id) {
        // docMapperCust.increaseVoteCount(id);
        // 远程IP+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 5000)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        // 推送消息
        Doc docDb = docMapper.selectByPrimaryKey(id);
        //取出流水号
        String logId = MDC.get("LOG_ID");
        //将流水号注入
        wsService.sendInfo("【" + docDb.getName() + "】被点赞！", logId);

    }


    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }

}
