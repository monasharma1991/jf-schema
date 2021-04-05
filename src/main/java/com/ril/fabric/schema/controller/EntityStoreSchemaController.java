package com.ril.fabric.schema.controller;

import com.ril.fabric.schema.interfaces.EntityStoreSchemaInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/schema/entity")
public class EntityStoreSchemaController {

    private EntityStoreSchemaInterface entityStoreSchemaInterface;

    public EntityStoreSchemaController(EntityStoreSchemaInterface entityStoreSchemaInterface) {
        this.entityStoreSchemaInterface = entityStoreSchemaInterface;
    }

    @PutMapping("/{schemaId}")
    public ResponseEntity<?> saveKeyToEntitySchema(@PathVariable int schemaId, @RequestParam String type,
                                                   @RequestBody Set<String> entities) {
        return entityStoreSchemaInterface.addEntitiesToSchema(schemaId, entities, type);
    }

    @PutMapping("/search")
    public ResponseEntity<?> searchByEntityType(@RequestParam String phrase) {
        return entityStoreSchemaInterface.searchByEntityType(phrase);
    }
}
