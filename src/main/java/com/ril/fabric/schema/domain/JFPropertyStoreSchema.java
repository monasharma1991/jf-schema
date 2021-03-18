package com.ril.fabric.schema.domain;

import com.ril.fabric.schema.pojo.QuantityTemplate;
import lombok.Data;

import java.util.Map;

@Data
public class JFPropertyStoreSchema extends JFSchemaObject{
    private Map<String, QuantityTemplate> value;
}
