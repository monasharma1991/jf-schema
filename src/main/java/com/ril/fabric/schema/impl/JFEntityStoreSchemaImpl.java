package com.ril.fabric.schema.impl;


import com.ril.fabric.schema.domain.JFEntityStoreSchema;
import com.ril.fabric.schema.interfaces.JFEntityStoreSchemaInterface;
import com.ril.fabric.schema.pojo.Status;

import java.util.List;

public class JFEntityStoreSchemaImpl extends JFSchemaImpl implements JFEntityStoreSchemaInterface {

    JFEntityStoreSchema entityStoreSchema;

    public JFEntityStoreSchemaImpl(JFEntityStoreSchema schema) {
        super(schema);
        this.entityStoreSchema = schema;
    }

    @Override
    public String generateSchemaName() {
        List<String> keys = entityStoreSchema.getKeys();
        return String.join("_", keys);       // "cell_customer_grid"    }
    }

    @Override
    public Status addEntityType(String entityType) {
        return addKey(entityType);
    }
}
