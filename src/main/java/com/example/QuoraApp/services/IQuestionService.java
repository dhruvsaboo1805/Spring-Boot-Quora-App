package com.example.QuoraApp.services;

import com.example.QuoraApp.dto.QuestionRequestDTO;
import com.example.QuoraApp.dto.QuestionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IQuestionService {
    Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO);
    Flux<QuestionResponseDTO> searchQuestions(String query , int pageOffset , int size);
    Flux<QuestionResponseDTO> getAllQuestions(String cursor , int size);
}
