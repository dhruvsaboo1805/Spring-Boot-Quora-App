package com.example.QuoraApp.services;

import com.example.QuoraApp.dto.PaginationResponseDTO;
import com.example.QuoraApp.dto.QuestionRequestDTO;
import com.example.QuoraApp.dto.QuestionResponseDTO;
import com.example.QuoraApp.models.QuestionElasticSearchDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface IQuestionService {
    Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO);
    Flux<QuestionResponseDTO> searchQuestions(String query , int pageOffset , int size);
    Flux<QuestionResponseDTO> getAllQuestions(String cursor , int size);
    Mono<QuestionResponseDTO> getQuestionById(String id);
    Mono<Void> deleteQuestionById(String id);
    Mono<PaginationResponseDTO<QuestionResponseDTO>> getQuestionsByTag(String tag , int pageOffset , int size);
    List<QuestionElasticSearchDocument> searchQuestionsByElasticsearch(String query);
}
