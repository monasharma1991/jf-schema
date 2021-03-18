package com.ril.fabric.schema.interfaces;

import com.ril.fabric.schema.domain.JFSchemaObject;
import com.ril.fabric.schema.pojo.Status;

public interface JFSchemaInterface<T> {
    JFSchemaObject getObject();
    Status addKey(String key);                             // key-only mode
    Status addKeyValue(String entity, T value);            // kv mode
    Status deleteKey(String key);
    Boolean hasKey(String key);
    Object getValue(String key);
    int getIndex(String key);
    int size();		                           // returns: key.size()
    void finalizeSchema();	                    // sorts all keys, updates index
}
