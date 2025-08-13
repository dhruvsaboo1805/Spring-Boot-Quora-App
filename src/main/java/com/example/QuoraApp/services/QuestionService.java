package com.example.QuoraApp.services;

import com.example.QuoraApp.dto.QuestionRequestDTO;
import com.example.QuoraApp.dto.QuestionResponseDTO;
import com.example.QuoraApp.mapper.QuestionMapper;
import com.example.QuoraApp.models.Question;
import com.example.QuoraApp.repositories.IQuestionRepository;
import com.example.QuoraApp.utils.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class QuestionService implements IQuestionService{

    private final IQuestionRepository questionRepository;
    @Override
    public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO) {
        Question question = Question.builder()
                .title(questionRequestDTO.getTitle())
                .content(questionRequestDTO.getContent())
                .tags(questionRequestDTO.getTags())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return questionRepository.save(question)
            .map(QuestionMapper::toQuestionResponseDTO)
            .doOnSuccess(response -> System.out.println("Question created successfully: " + response))
            .doOnError(error -> System.out.println("Error creating question: " + error));
    }

    @Override
    public Flux<QuestionResponseDTO> searchQuestions(String query, int pageOffset, int size) {
        return questionRepository.findByTitleOrContentContainingIgnoreCase(query , PageRequest.of(pageOffset , size))
                .map(QuestionMapper::toQuestionResponseDTO)
                .doOnError(error -> System.out.println("Error searching questions: " + error))
                .doOnComplete(() -> System.out.println("Questions searched successfully"));
    }

    @Override
    public Flux<QuestionResponseDTO> getAllQuestions(String cursor, int size) {
        Pageable pageable = PageRequest.of(0 , size);
        if(!CursorUtils.isValidCursor(cursor)) {
            return questionRepository.findTop10ByOrderByCreatedAtAsc()
                    .take(size)
                    .map(QuestionMapper::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error fetching questions: " + error))
                    .doOnComplete(() -> System.out.println("Questions fetched successfully"));
        } else {
            LocalDateTime cursorTimeStamp = CursorUtils.parseCursor(cursor);
            return questionRepository.findByCreatedAtGreaterThanOrderByCreatedAtAsc(cursorTimeStamp, pageable)
                    .map(QuestionMapper::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error fetching questions: " + error))
                    .doOnComplete(() -> System.out.println("Questions fetched successfully"));
        }

    }

    @Override
    public Mono<QuestionResponseDTO> getQuestionById(String id) {
        return questionRepository.findById(id)
                .map(QuestionMapper::toQuestionResponseDTO)
                .doOnSuccess(response -> System.out.println("Question Fetched successfully: " + response))
                .doOnError(error -> System.out.println("Error Fetching question: " + error));
    }

    @Override
    public Mono<Void> deleteQuestionById(String id) {
        return questionRepository.deleteById(id)
                .doOnSuccess(response -> System.out.println("Question Fetched successfully: " + response))
                .doOnError(error -> System.out.println("Error Fetching question: " + error));
    }

    @Override
    public Flux<QuestionResponseDTO> getQuestionsByTag(String tag, int pageOffset, int size) {
        return questionRepository.findByTagsContainingIgnoreCase(tag , PageRequest.of(pageOffset , size))
                .map(QuestionMapper::toQuestionResponseDTO)
                .doOnError(error -> System.out.println("Error searching questions with request to tags: " + error))
                .doOnComplete(() -> System.out.println("Questions searched successfully with request to tags"));
    }
}
