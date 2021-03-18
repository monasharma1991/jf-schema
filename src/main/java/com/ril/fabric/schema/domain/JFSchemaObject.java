package com.ril.fabric.schema.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JFSchemaObject {
    private boolean isFinalise;
    private String schemaName;
    private List<String> keys;
    private Map<String, Integer> index;
   // private Map<String, T> value;
}