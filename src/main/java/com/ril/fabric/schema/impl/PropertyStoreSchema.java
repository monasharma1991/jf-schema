package com.ril.fabric.schema.impl;

import com.jio.protos.commons.QtyNumeric;
import com.jio.protos.commons.Quantity;
import com.jio.protos.fabric.event.LogEventSchema;
import com.ril.fabric.schema.interfaces.PropertyStoreSchemaInterface;

public class PropertyStoreSchema implements PropertyStoreSchemaInterface {


    public void addPropertyToLogSchema(String logSchemaId, String propertyName, String type, String unit, String quantityType, String quantitySubType) {

        LogEventSchema.JFLogEventSchema.Builder logEventSchemaBuilder = LogEventSchema.JFLogEventSchema.newBuilder();

        // JFPropertyStoreSchema propertyStoreSchema =
         //  Quantity quantity = parseQuantity(quantityType, quantitySubType);
    }

 /*   private Quantity parseQuantity(String quantityType, String quantitySubType) {
        Quantity.Builder builder = Quantity.newBuilder();
        if (quantityType == "Numeric")
            builder.setNumeric(QtyNumeric.newBuilder().build());
        if (quantitySubType == "float")


    }*/
}
