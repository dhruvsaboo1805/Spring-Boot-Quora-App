package com.example.QuoraApp.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class QuestionResponseDTO {
    private String id;
    private String title;
    private String content;
    List<String> tags;
    private LocalDateTime createdAt;
}
