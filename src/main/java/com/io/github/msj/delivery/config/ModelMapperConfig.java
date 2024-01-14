package com.io.github.msj.delivery.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        return modelMapper;
//    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }

}
