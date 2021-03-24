package com.ril.fabric.schema.domain;

import lombok.Data;

@Data
public class QuantityTemplate {

    private String propertyName;
    private String type;
    private String unit;
    private JfQuantityType quantityType;
    private JfQuantitySubType quantitySubType;

  public  enum JfQuantityType {
        QtyNumeric,
        QtySymbolic,
        QtyOrdinal,
        QtySpatial,
        QtyTemporal,
        QtyMonetary,
        QtyDemographic;
    }

   public enum JfQuantitySubType {
        float_value,
        double_value,
        uint64_value,
        uint32_value,
        sint64_value,
        sint32_value,
        value,
        date,
        time,
        date_time,
        date_range,
        time_range,
        timestamp,
        duration;
    }

}
