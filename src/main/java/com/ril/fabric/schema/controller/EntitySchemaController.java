package com.ril.fabric.schema.controller;

import com.ril.fabric.schema.controller.service.EntitySchemaService;
import com.ril.fabric.schema.domain.JFEntityStoreSchema;
import com.ril.fabric.schema.impl.JFEntityStoreSchemaImpl;
import com.ril.fabric.schema.pojo.LogSchemaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntitySchemaController {

    @Autowired
    EntitySchemaService entitySchemaService;

    @PutMapping("/v2/{logSchemaId}")
    public void addEntityKeys_2(@PathVariable String logSchemaId, String entityKey) {
        LogSchemaEntity logSchemaEntity = getLogSchemaById(logSchemaId);
        JFEntityStoreSchema jfEntityStoreSchema = logSchemaEntity.getJfEntityStoreSchema();
        entitySchemaService.addKeys(entityKey, jfEntityStoreSchema);
        saveLogSchema(logSchemaEntity);
    }

    private void saveLogSchema(LogSchemaEntity logSchemaEntity) {
        // save to db
    }

    private LogSchemaEntity getLogSchemaById(String logSchemaId) {
        // get it from DB
        return new LogSchemaEntity();
    }
}
