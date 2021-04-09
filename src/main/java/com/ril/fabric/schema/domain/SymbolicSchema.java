package com.ril.fabric.schema.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SymbolicSchema extends QuantitySchemaBaseObject{
    private List<String> keyList;
}
