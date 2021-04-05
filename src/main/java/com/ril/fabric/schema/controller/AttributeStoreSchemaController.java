package com.ril.fabric.schema.controller;

import com.ril.fabric.schema.domain.QuantityTemplate;
import com.ril.fabric.schema.interfaces.AttributeStoreSchemaInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/schema/attribute")
public class AttributeStoreSchemaController {

    @Autowired
    private AttributeStoreSchemaInterface attributeStoreSchemaInterface;

    @PutMapping("/{schemaId}")
    public ResponseEntity<?> addAttributeToSchema(@PathVariable int schemaId, @RequestParam String entityType, @RequestParam String type, @RequestBody QuantityTemplate quantityTemplate) {
        return attributeStoreSchemaInterface.addAttributeToSchema(schemaId, entityType, type, quantityTemplate);
    }
}
