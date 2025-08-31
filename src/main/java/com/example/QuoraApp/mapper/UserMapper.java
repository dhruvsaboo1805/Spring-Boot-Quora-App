package com.example.QuoraApp.mapper;

import com.example.QuoraApp.dto.CreateUserResponseDTO;
import com.example.QuoraApp.models.User;

public class UserMapper {
    public static CreateUserResponseDTO toUserResponseDTO(User user) {
        return CreateUserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .followedQuestionIds(user.getFollowedQuestionIds())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
