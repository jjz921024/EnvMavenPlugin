package io.jjz.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class EvnConfig {

  @PostConstruct
  public void init() {
    System.out.println("!!!!!" + System.getenv("TZ"));
  }
}
