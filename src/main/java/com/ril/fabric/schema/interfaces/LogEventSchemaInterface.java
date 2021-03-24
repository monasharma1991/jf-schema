package com.ril.fabric.schema.interfaces;

import org.springframework.http.ResponseEntity;

public interface LogEventSchemaInterface {
    ResponseEntity<?> createLogEventSchema(String vertical, String source, String domain);
    ResponseEntity<?> setFinaliseAndIndex(String logEventSchemaId);
    ResponseEntity<?> getSchemaDocumentById(String logEventSchemaId);
    ResponseEntity<?> getLogSchemaProtoById(String logEventSchemaId);
}
