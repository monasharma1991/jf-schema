package com.ril.fabric.schema.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumericSchema extends QuantitySchemaBaseObject{
    private int minValue;
    private int maxValue;
}
