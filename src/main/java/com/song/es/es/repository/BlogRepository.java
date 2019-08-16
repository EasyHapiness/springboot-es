package com.song.es.es.repository;

import com.song.es.es.entity.BlogModel;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Desc
 * @Author
 * @Date 2019/8/15
 */
public interface BlogRepository extends ElasticsearchRepository<BlogModel, String> {

    @Query("{\"match_phrase\":{\"title\":\"?0\"}}")
    List<BlogModel> findByTitleLike(String keyword);

}
