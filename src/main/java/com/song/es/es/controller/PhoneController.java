package com.song.es.es.controller;

import com.song.es.es.client.HttpClientForData;
import com.song.es.es.entity.PhoneModel;
import com.song.es.es.uitils.DateUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Desc
 * @Author
 * @Date 2019/8/16
 */
@RestController
@RequestMapping(value = "/phone")
@CrossOrigin
public class PhoneController extends BaseController {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    HttpClientForData httpClientForData;

    @GetMapping("/huawei")
    public void getDataFromHuawei() throws IOException {
        httpClientForData.huawei();
    }

    /**
     * 全文搜索
     *
     * @param keyword 关键字
     * @param page    当前页，从0开始
     * @param size    每页大小
     * @return {@link } 接收到的数据格式为json
     */
    @GetMapping("/full")
    @ResponseBody
    public Object full(String keyword, Integer page, Integer size) {

        // 校验参数
        if (StringUtils.isEmpty(page))
            page = 0; // if page is null, page = 0

        if (StringUtils.isEmpty(size))
            size = 10; // if size is null, size default 10

        // 构造分页类
        Pageable pageable = PageRequest.of(page, size);

        // 构造查询 NativeSearchQueryBuilder
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
                .withPageable(pageable);
        if (!StringUtils.isEmpty(keyword)) {
            // keyword must not null
            searchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(keyword));
        }

        /*
        SearchQuery
        这个很关键，这是搜索条件的入口，
        elasticsearchTemplate 会 使用它 进行搜索
         */
        SearchQuery searchQuery = searchQueryBuilder.build();

        Page<PhoneModel> phoneModelPage = elasticsearchTemplate.queryForPage(searchQuery, PhoneModel.class);

        return getSuccessResult(phoneModelPage);
    }


    /**
     * 高级搜索，根据字段进行搜索
     * @param name 名称
     * @param color 颜色
     * @param sellingPoint 卖点
     * @param price 价格
     * @param start 开始时间(格式：yyyy-MM-dd HH:mm:ss)
     * @param end 结束时间(格式：yyyy-MM-dd HH:mm:ss)
     * @param page 当前页，从0开始
     * @param size 每页大小
     * @return {@link }
     */
    @GetMapping("/_search")
    public Object search(String name, String color, String sellingPoint, String price, String start, String end, Integer page, Integer size) {

        // 校验参数
        if (StringUtils.isEmpty(page) || page < 0)
            page = 0; // if page is null, page = 0

        if (StringUtils.isEmpty(size) || size < 0)
            size = 10; // if size is null, size default 10

        // 构造分页对象
        Pageable pageable = PageRequest.of(page, size);

        // BoolQueryBuilder (Elasticsearch Query)
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (!StringUtils.isEmpty(name)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", name));
        }

        if (!StringUtils.isEmpty(color)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("colors", color));
        }

        if (!StringUtils.isEmpty(sellingPoint)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("sellingPoints", sellingPoint));
        }

        if (!StringUtils.isEmpty(price)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("price", price));
        }

        if (!StringUtils.isEmpty(start)) {
            Date startTime = DateUtils.getDateFromStr(start);
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").gt(startTime.getTime()));
        }

        if (!StringUtils.isEmpty(end)) {
            Date endTime = DateUtils.getDateFromStr(end);
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").lt(endTime.getTime()));
        }

        // BoolQueryBuilder (Spring Query)
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build()
                ;

        // page search
        Page<PhoneModel> phoneModelPage = elasticsearchTemplate.queryForPage(searchQuery, PhoneModel.class);

        // return
        return getSuccessResult(phoneModelPage);
    }


    /*
     * index 索引index
     * text 需要被分析的词语
     * 默认使用中文ik_smart分词
     * */
    @GetMapping("/search/fenci")
    public Object getAnalyzes(String index,String text){

        //调用ES客户端分词器进行分词
        AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(elasticsearchTemplate.getClient(),
                AnalyzeAction.INSTANCE,index,text).setAnalyzer("ik_smart");
        List<AnalyzeResponse.AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();

        // 赋值
        List<String> searchTermList = new ArrayList<>();
        ikTokenList.forEach(ikToken -> { searchTermList.add(ikToken.getTerm()); });
        searchTermList.toArray(new String[searchTermList.size()]);
        return getSuccessResult(searchTermList);
    }

    /*
     * Class clazz指定的索引index实体类类型
     * String text 搜索建议关键词
     * */
    public String[] getSuggestion(Class clazz,String text){
        //构造搜索建议语句
        SuggestionBuilder completionSuggestionFuzzyBuilder = SuggestBuilders.completionSuggestion("suggest").prefix(text, Fuzziness.AUTO);

        //根据
        final SearchResponse suggestResponse = elasticsearchTemplate.suggest(new SuggestBuilder().addSuggestion("my-suggest",completionSuggestionFuzzyBuilder), clazz);
        CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("my-suggest");
        List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
        System.err.println(options);
        System.out.println(options.size());
        System.out.println(options.get(0).getText().string());

        List<String> suggestList = new ArrayList<>();
        options.forEach(item ->{ suggestList.add(item.getText().toString()); });

        return suggestList.toArray(new String[suggestList.size()]);
    }
}
