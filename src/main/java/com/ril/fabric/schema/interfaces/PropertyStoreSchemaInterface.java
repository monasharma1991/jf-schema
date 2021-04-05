package com.ril.fabric.schema.interfaces;

import com.ril.fabric.schema.domain.QuantityTemplate;
import org.springframework.http.ResponseEntity;

public interface PropertyStoreSchemaInterface {
    ResponseEntity<?> addPropertyToSchema(int schemaId, QuantityTemplate quantityTemplate, String type);
}
