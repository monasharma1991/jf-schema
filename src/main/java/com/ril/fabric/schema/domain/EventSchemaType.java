package com.ril.fabric.schema.domain;

public class EventSchemaType {

    private static String logSchemaCollection = "jf_schema";
    private static final String pivotSchemaCollection = "pivotSchema";
    private static final String fabricQuantitySchema = "quantitySchema";

    public static String getPivotSchemaCollection() {
        return pivotSchemaCollection;
    }

    public static String getLogSchemaCollection() {
        return logSchemaCollection;
    }

    public static String getFabricQuantitySchema(){
        return fabricQuantitySchema;
    }

}
