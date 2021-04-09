package com.ril.fabric.schema.controller;

import com.ril.fabric.schema.domain.NumericSchema;
import com.ril.fabric.schema.domain.SymbolicSchema;
import com.ril.fabric.schema.interfaces.QuantitySchemaTableInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/quantitySchema")
public class QuantitySchemaTableController {

    private final QuantitySchemaTableInterface quantitySchemaTableInterface;

    @Autowired
    public QuantitySchemaTableController(QuantitySchemaTableInterface quantitySchemaTableInterface) {
        this.quantitySchemaTableInterface = quantitySchemaTableInterface;
    }

    @PostMapping("/numeric")
    public ResponseEntity<?> createNumericQuantitySchemaTable(@RequestBody NumericSchema numericSchema) {
        return quantitySchemaTableInterface.createNumericQuantitySchemaTable(numericSchema);
    }

    @PostMapping("/symbolic")
    public ResponseEntity<?> createSymbolicQuantitySchemaTable(@RequestBody SymbolicSchema symbolicSchema){
        return quantitySchemaTableInterface.createSymbolicQuantitySchemaTable(symbolicSchema);
    }
}
