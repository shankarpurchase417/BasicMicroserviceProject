package com.tcg.order.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.tcg.order.dto.OrderDto;
import com.tcg.order.entity.Order;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
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
