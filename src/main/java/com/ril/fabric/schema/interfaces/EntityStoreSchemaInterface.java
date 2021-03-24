package com.ril.fabric.schema.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface EntityStoreSchemaInterface {
    ResponseEntity<?> addEntitiesToLogSchema(String logSchemaId, Set<String> entities);
    ResponseEntity<?> addEntityToLogSchema(String schemaId, String entity);
    ResponseEntity<?> searchByEntityType(String phrase);

    // pivot apis
    ResponseEntity<?> addEntitiesToPivotSchema(String pivotSchemaId, Set<String> entities);

}
