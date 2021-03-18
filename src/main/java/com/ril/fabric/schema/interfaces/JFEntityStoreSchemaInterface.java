package com.ril.fabric.schema.interfaces;

import com.ril.fabric.schema.pojo.Status;

public interface JFEntityStoreSchemaInterface {
    String generateSchemaName();
    Status addEntityType(String entityType);
}
