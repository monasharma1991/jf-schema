package com.ril.fabric.schema.pojo;

import com.ril.fabric.schema.domain.JFAttributeStoreSchema;
import com.ril.fabric.schema.domain.JFEntityStoreSchema;
import com.ril.fabric.schema.domain.JFPropertyStoreSchema;
import lombok.Data;

@Data
public class LogSchemaEntity extends AbstractEntity{

    JFEntityStoreSchema jfEntityStoreSchema;
    JFPropertyStoreSchema jfPropertyStoreSchema;
    JFAttributeStoreSchema jfAttributeStoreSchema;
}
