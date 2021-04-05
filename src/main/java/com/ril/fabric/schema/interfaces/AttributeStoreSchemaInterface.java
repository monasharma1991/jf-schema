package com.ril.fabric.schema.interfaces;

import com.ril.fabric.schema.domain.QuantityTemplate;
import org.springframework.http.ResponseEntity;

public interface AttributeStoreSchemaInterface {
    ResponseEntity<?> addAttributeToSchema(int schemaId, String entityType, String type, QuantityTemplate quantityTemplate);
}
