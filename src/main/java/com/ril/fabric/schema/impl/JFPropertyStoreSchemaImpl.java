package com.ril.fabric.schema.impl;

import com.ril.fabric.schema.domain.JFPropertyStoreSchema;
import com.ril.fabric.schema.pojo.QuantityTemplate;
import com.ril.fabric.schema.pojo.Status;

public class JFPropertyStoreSchemaImpl extends JFSchemaImpl<QuantityTemplate> {

    JFPropertyStoreSchema propertyStoreSchema;

    public JFPropertyStoreSchemaImpl(JFPropertyStoreSchema schema) {
        super(schema);
        this.propertyStoreSchema = schema;
    }

    @Override
    public Status addKeyValue(String entity, QuantityTemplate value) {
        if (!hasKey(entity))
            return new Status("Key doesn't exists in the schema. Please add the key first.");
        else {
            this.propertyStoreSchema.getValue().put(entity, value);
            return new Status("value added in the schema");
        }
    }

    @Override
    public Object getValue(String key) {
        return super.getValue(key);
    }
}
