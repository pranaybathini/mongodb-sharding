package com.mongo.mongosharding.properties;

import com.mongo.mongosharding.properties.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;


import lombok.Data;

@Data
@ConfigurationProperties
public class ApplicationProperties {
  private MongoProperties mongodb;
}


