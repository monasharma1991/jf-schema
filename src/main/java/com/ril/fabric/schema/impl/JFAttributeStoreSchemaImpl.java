package com.ril.fabric.schema.impl;


import com.ril.fabric.schema.domain.JFAttributeStoreSchema;
import com.ril.fabric.schema.domain.JFPropertyStoreSchema;
import com.ril.fabric.schema.pojo.Status;

public class JFAttributeStoreSchemaImpl extends JFSchemaImpl<JFPropertyStoreSchema> {

    JFAttributeStoreSchema attributeStoreSchema;

    public JFAttributeStoreSchemaImpl(JFAttributeStoreSchema schema) {
        super(schema);
        this.attributeStoreSchema = schema;
    }

    @Override
    public Status addKeyValue(String entity, JFPropertyStoreSchema propertyStoreSchema) {
        if (!hasKey(entity))
            return new Status("Key doesn't exists in the schema. Please add the key first.");
        else {
            this.attributeStoreSchema.getEntityAttributes().put(entity, propertyStoreSchema);
            return new Status("value added in the schema");
        }
    }

    public void addEntityAttributes(String key, JFPropertyStoreSchema propertyStoreSchema) {
        attributeStoreSchema.getEntityAttributes().put(key, propertyStoreSchema);
    }

    @Override
    public Object getValue(String key) {
        return super.getValue(key);
    }
}
