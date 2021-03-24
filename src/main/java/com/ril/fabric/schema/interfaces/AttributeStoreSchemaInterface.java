package com.ril.fabric.schema.interfaces;

import com.ril.fabric.schema.domain.QuantityTemplate;
import org.springframework.http.ResponseEntity;

public interface AttributeStoreSchemaInterface {
    ResponseEntity<?> addAttributeToSchema(String logSchemaId, String entityType, QuantityTemplate quantityTemplate);
}
