package com.example.QuoraApp.services;

import com.example.QuoraApp.dto.CreateUserRequestDTO;
import com.example.QuoraApp.dto.CreateUserResponseDTO;
import com.example.QuoraApp.dto.QuestionResponseDTO;
import com.example.QuoraApp.mapper.QuestionMapper;
import com.example.QuoraApp.mapper.UserMapper;
import com.example.QuoraApp.models.User;
import com.example.QuoraApp.repositories.IQuestionRepository;
import com.example.QuoraApp.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final IUserRepository userRepository;
    private final IQuestionRepository questionRepository;

    @Override
    public Mono<CreateUserResponseDTO> createUser(CreateUserRequestDTO createUserRequestDTO) {
        User user = User.builder()
                .name(createUserRequestDTO.getName())
                .phoneNumber(createUserRequestDTO.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return userRepository.save(user)
                .map(UserMapper::toUserResponseDTO)
                .doOnSuccess(response -> System.out.println("User created successfully: " + response))
                .doOnError(error -> System.out.println("Error creating user: " + error));
    }

    @Override
    public Mono<CreateUserResponseDTO> followQuestion(String userId, String questionId) {
        return userRepository.findById(userId)
                .zipWith(questionRepository.existsById(questionId), (user, questionExists) -> {
                    if (!questionExists) {
                        throw new RuntimeException("Question not found with id: " + questionId);
                    }
                    return user;
                })
                .flatMap(user -> {
                    // Add the questionId to the user's followed list if not already present
                    if (!user.getFollowedQuestionIds().contains(questionId)) {
                        user.getFollowedQuestionIds().add(questionId);
                        return userRepository.save(user);
                    }
                    return Mono.just(user); // Return the user without changes
                })
                .map(UserMapper::toUserResponseDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + userId)));
    }

    @Override
    public Flux<QuestionResponseDTO> getUserFeeds(String userId) {
        return userRepository.findById(userId)
                .flatMapMany(user -> {
                    List<String> followedQuestionIds = user.getFollowedQuestionIds();
                    if (followedQuestionIds == null || followedQuestionIds.isEmpty()) {
                        return Flux.empty();
                    }
                    return questionRepository.findAllById(followedQuestionIds);
                })
                .map(QuestionMapper::toQuestionResponseDTO)
                .switchIfEmpty(Flux.error(new RuntimeException("User not found with id: " + userId)));
    }

    @Override
    public Mono<Void> followUser(String followerId, String followingId) {

        Mono<User> followerMono = userRepository.findById(followerId);
        Mono<User> followingMono = userRepository.findById(followingId);

        return Mono.zip(followerMono, followingMono)
                .flatMap(tuple -> {
                    User follower = tuple.getT1();
                    User following = tuple.getT2();

                    if (!follower.getFollowingUserIds().contains(followingId)) {
                        follower.getFollowingUserIds().add(followingId);
                    }
                    if (!following.getFollowerUserIds().contains(followerId)) {
                        following.getFollowerUserIds().add(followerId);
                    }

                    return userRepository.save(follower).then(userRepository.save(following));
                }).then();
    }
}
