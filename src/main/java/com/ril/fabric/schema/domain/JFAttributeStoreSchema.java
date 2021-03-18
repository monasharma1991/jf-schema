package com.ril.fabric.schema.domain;

import lombok.Data;

import java.util.Map;

@Data
public class JFAttributeStoreSchema extends JFSchemaObject {
   private Map<String, JFPropertyStoreSchema> entityAttributes;
}
