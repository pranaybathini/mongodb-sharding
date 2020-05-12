package com.mongo.mongosharding.properties.mongo;

import java.util.List;

import lombok.Data;

@Data
public class MongoProperties {

  private String uri;
  private String uriSlave;
  private String database;
  private List<ShardCollection> shardCollections;
}
