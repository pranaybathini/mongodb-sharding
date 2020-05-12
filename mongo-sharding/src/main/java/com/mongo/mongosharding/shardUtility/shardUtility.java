package com.mongo.mongosharding.shardUtility;

import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongo.mongosharding.properties.ApplicationProperties;
import com.mongo.mongosharding.properties.mongo.ShardCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Bathini Pranay kumar
 */

@Slf4j
@Component
public class shardUtility {


  @Autowired
  ApplicationProperties applicationProperties;

  private MongoClient mongoClient;

  private MongoDatabase mongoDatabase;

  private Document result;

  public void setShardKey(String collectionName, String shardField) {
    mongoDatabase = getMongoDatabase();
    final BasicDBObject shardKey = new BasicDBObject(shardField, "hashed");
    final BasicDBObject cmd = new BasicDBObject("shardCollection",
        applicationProperties.getMongodb().getDatabase() + "." + collectionName);
    cmd.put("key", shardKey);
    try {
      result = mongoDatabase.runCommand(cmd);
      log.info("Added shard key: {} for collection {}, result: {}", shardField, collectionName,
          result);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public MongoDatabase getMongoDatabase() {
    mongoClient = getMongoClient();
    mongoDatabase = mongoClient.getDatabase("admin");
    return mongoDatabase;
  }

  public void enableSharding(String dbName) {
    mongoDatabase = getMongoDatabase();
    final BasicDBObject cmd = new BasicDBObject("enableSharding", dbName);
    Document document = mongoDatabase.runCommand(cmd);
    System.out.println(document);
    log.info("Enabled sharding on database:{} status:{}", dbName, document);
  }

  public MongoClient getMongoClient() {
    log.info("Initializing mongodb client");
    if (mongoClient != null)
      return mongoClient;
    try {
      MongoClientURI mongoClientURI =
          new MongoClientURI(applicationProperties.getMongodb().getUri());
      mongoClient = new MongoClient(mongoClientURI);
    } catch (Exception e) {
      log.error("Unable to initialize client");
      throw new RuntimeException("Unable to initialize client");
    }
    return mongoClient;
  }

  @PostConstruct
  public void init() {
    enableSharding(applicationProperties.getMongodb().getDatabase());
    List<ShardCollection> shardCollections =
        applicationProperties.getMongodb().getShardCollections();
    shardCollections.forEach(shardCollection -> {
      setShardKey(shardCollection.getCollection(), shardCollection.getShardField());
    });
  }

}
