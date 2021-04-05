package com.ril.fabric.schema.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface EntityStoreSchemaInterface {
    ResponseEntity<?> addEntitiesToSchema(int schemaId, Set<String> entities, String type);
    ResponseEntity<?> addEntityToSchema(int schemaId, String entity, String type);
    ResponseEntity<?> searchByEntityType(String phrase);

}
