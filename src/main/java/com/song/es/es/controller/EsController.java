package com.song.es.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.song.es.es.entity.BlogModel;
import com.song.es.es.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * @Desc
 * @Author
 * @Date 2019/8/15
 */
@Controller
@RequestMapping("/blog")
public class EsController extends BaseController{

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @RequestMapping("/add")
    @ResponseBody
    public Object add() {
        BlogModel blogModel = new BlogModel();
        blogModel.setTime(new Date());
        blogModel.setTitle("Elasticsearch实战篇：Spring Boot整合ElasticSearch");
        blogRepository.save(blogModel);
        return getSuccessResult();
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public Object getById(@PathVariable String id) {

        if (StringUtils.isEmpty(id)){
            return getFailResult();
        }
        Optional<BlogModel> blogModelOptional = blogRepository.findById(id);
        if (blogModelOptional.isPresent()) {
            BlogModel blogModel = blogModelOptional.get();
            return getSuccessResult(blogModel);
        }
        return getFailResult();
    }

    @GetMapping("/get")
    @ResponseBody
    public JSONObject getAll() {
        Iterable<BlogModel> iterable = blogRepository.findAll();
        List<BlogModel> list = new ArrayList<>();
        iterable.forEach(list::add);
        return getSuccessResult(list);
    }

    @PostMapping("/update")
    public Object updateById(@RequestBody BlogModel blogModel) {
        String id = blogModel.getId();
        if (StringUtils.isEmpty(id))
            return getFailResult();
        blogRepository.save(blogModel);
        return getSuccessResult();
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteById(@PathVariable String id) {
        if (StringUtils.isEmpty(id))
            return getFailResult();
        blogRepository.deleteById(id);
        return getSuccessResult();
    }

    @DeleteMapping("/delete")
    public Object deleteById() {
        blogRepository.deleteAll();
        return getSuccessResult();
    }

    @GetMapping("/search/title/{keyword}")
    @ResponseBody
    public Object repSearchTitle(@PathVariable String keyword) {
        if (StringUtils.isEmpty(keyword))
            return getFailResult();
        return getSuccessResult(blogRepository.findByTitleLike(keyword));
    }

    @GetMapping("/title/{keyword}")
    @ResponseBody
    public Object searchTitle(@PathVariable String keyword) {
        if (StringUtils.isEmpty(keyword))
            return getFailResult();

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryStringQuery(keyword))
                .build();
        List<BlogModel> list = elasticsearchTemplate.queryForList(searchQuery, BlogModel.class);
        return getSuccessResult(list);
    }
}
