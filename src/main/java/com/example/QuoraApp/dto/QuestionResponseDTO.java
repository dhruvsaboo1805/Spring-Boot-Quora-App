package com.example.QuoraApp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class QuestionResponseDTO {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
