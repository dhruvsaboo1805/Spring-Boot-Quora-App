package com.example.QuoraApp.services;

import com.example.QuoraApp.models.Question;
import com.example.QuoraApp.models.QuestionElasticSearchDocument;
import com.example.QuoraApp.repositories.IQuestionElasticSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionElasticSearchIndexService implements IQuestionElasticSearchIndexService {

    private final IQuestionElasticSearchRepository questionElasticSearchRepository;

    @Override
    public void createQuestionIndex(Question question) {
        QuestionElasticSearchDocument questionElasticSearchDocument = QuestionElasticSearchDocument.builder()
                .Id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .build();
        questionElasticSearchRepository.save(questionElasticSearchDocument);
    }
}
