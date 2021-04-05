package com.ril.fabric.schema.interfaces;

import org.springframework.http.ResponseEntity;

public interface EventSchemaInterface {
    ResponseEntity<?> createEventSchema(String name);
    ResponseEntity<?> setFinaliseAndIndex(int eventSchemaId);
    ResponseEntity<?> getSchemaDocumentById(int eventSchemaId);
    ResponseEntity<?> getSchemaProtoById(int eventSchemaId);
    ResponseEntity<?> getAllSchemas();
}
