package com.mongo.mongosharding;

import com.mongo.mongosharding.properties.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:application.yml"})
@EnableConfigurationProperties(ApplicationProperties.class)
public class MongoShardingApplication {

  public static void main(String[] args) {
    SpringApplication.run(MongoShardingApplication.class, args);
  }
}
