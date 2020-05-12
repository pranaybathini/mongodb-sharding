package com.mongo.mongosharding.properties.mongo;

import lombok.Data;

/**
 * @author Bathini Pranay kumar
 */
@Data
public class ShardCollection {
    private String collection;
    private String shardField;
}
