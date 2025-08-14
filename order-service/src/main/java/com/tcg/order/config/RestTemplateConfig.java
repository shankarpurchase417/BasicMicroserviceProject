package com.tcg.order.config;
import java.time.Duration;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import com.tcg.order.dto.OrderDto;
import com.tcg.order.entity.Order;

@Configuration
public class RestTemplateConfig {

    @Bean
    @Primary
    public RestTemplate defaultRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @Qualifier("userServiceRestTemplate")
    public RestTemplate userServiceRestTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(2)) // ✅ connection timeout
                .setReadTimeout(Duration.ofSeconds(5))    // ✅ read/response timeout
                .build();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Use strict matching so only exact names map
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Skip mapping for auto-generated ID
        mapper.addMappings(new PropertyMap<OrderDto, Order>() {
            @Override
            protected void configure() {
                skip(destination.getId()); // This avoids mapping the ID
            }
        });

        return mapper;
    }
}
