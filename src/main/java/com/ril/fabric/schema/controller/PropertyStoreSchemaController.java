package com.ril.fabric.schema.controller;

import com.ril.fabric.schema.domain.QuantityTemplate;
import com.ril.fabric.schema.interfaces.PropertyStoreSchemaInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/schema/property")
public class PropertyStoreSchemaController {

    @Autowired
    private PropertyStoreSchemaInterface propertyStoreSchemaInterface;

    @PutMapping("/{schemaId}")
    public ResponseEntity<?> addPropertyToSchema(@PathVariable int schemaId, @RequestParam String type, @RequestBody QuantityTemplate quantityTemplate) {
        return propertyStoreSchemaInterface.addPropertyToSchema(schemaId, quantityTemplate, type);
    }

}
