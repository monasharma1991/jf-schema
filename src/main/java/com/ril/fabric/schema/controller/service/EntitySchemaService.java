package com.ril.fabric.schema.controller.service;

import com.ril.fabric.schema.domain.JFEntityStoreSchema;
import org.springframework.stereotype.Service;

@Service
public class EntitySchemaService {

    public void addKeys(String entityKey, JFEntityStoreSchema jfEntityStoreSchema) {
        jfEntityStoreSchema.getKeys().add(entityKey);
    }
}
