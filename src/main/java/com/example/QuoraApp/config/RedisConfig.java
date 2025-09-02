package com.example.QuoraApp.config;

import com.example.QuoraApp.dto.QuestionResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, QuestionResponseDTO> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<QuestionResponseDTO> valueSerializer = new Jackson2JsonRedisSerializer<>(QuestionResponseDTO.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, QuestionResponseDTO> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);

        RedisSerializationContext<String, QuestionResponseDTO> context = builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
