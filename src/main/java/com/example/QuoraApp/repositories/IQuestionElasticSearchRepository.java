package com.example.QuoraApp.repositories;

import com.example.QuoraApp.models.QuestionElasticSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuestionElasticSearchRepository extends ElasticsearchRepository<QuestionElasticSearchDocument , String> {
    List<QuestionElasticSearchDocument> findByTitleContainingOrContentContaining(String title, String content);
}
