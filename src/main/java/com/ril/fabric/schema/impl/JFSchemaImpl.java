package com.ril.fabric.schema.impl;


import com.ril.fabric.schema.domain.JFSchemaObject;
import com.ril.fabric.schema.interfaces.JFSchemaInterface;
import com.ril.fabric.schema.pojo.Status;

public class JFSchemaImpl<T> implements JFSchemaInterface<T> {

    private JFSchemaObject schema;

    public JFSchemaImpl(JFSchemaObject schema) {
        this.schema = schema;
    }

    @Override
    public JFSchemaObject getObject() {
        return this.schema;
    }

    @Override
    public Status addKey(String key) {
        if (hasKey(key))
            return new Status("Key already exists in the schema");
        else {
            this.schema.getKeys().add(key);
            return new Status("Key added in the schema");
        }
    }

    @Override
    public Status addKeyValue(String entity, T value) {
        // No implementation for this
        return null;
    }

    @Override
    public Status deleteKey(String key) {
        if (this.schema.getKeys().remove(key))
            return new Status("Key " + key + " successfully removed from the schema.");
        else
            return new Status("Some error in removing the key : " + key);

    }

    @Override
    public Boolean hasKey(String key) {
        return this.schema.getKeys().contains(key);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public int getIndex(String key) {
        return 0;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void finalizeSchema() {

    }
}
