package com.alta.bootcamp.laundryapp.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyApplicationConfig {
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
