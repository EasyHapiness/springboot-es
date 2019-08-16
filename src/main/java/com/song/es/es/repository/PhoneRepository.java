package com.song.es.es.repository;

import com.song.es.es.entity.PhoneModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Desc
 * @Author
 * @Date 2019/8/16
 */
public interface PhoneRepository extends ElasticsearchRepository<PhoneModel, String> {
}
