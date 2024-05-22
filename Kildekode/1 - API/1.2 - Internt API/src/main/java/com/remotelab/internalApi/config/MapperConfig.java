package com.remotelab.internalApi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        // returns a new model mapper for the application context
        return new ModelMapper();
    }
}
