package com.ril.fabric.schema.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantitySchemaBaseObject {

    private String vertical;
    private String domain;
    private String group;
    private String name;
    private Unit unit;
}
