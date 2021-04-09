package com.ril.fabric.schema.interfaces;

import com.ril.fabric.schema.domain.NumericSchema;
import com.ril.fabric.schema.domain.SymbolicSchema;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuantitySchemaTableInterface {

    ResponseEntity<?> createNumericQuantitySchemaTable(NumericSchema numericSchema);

    ResponseEntity<?> createSymbolicQuantitySchemaTable(SymbolicSchema symbolicSchema);
}
