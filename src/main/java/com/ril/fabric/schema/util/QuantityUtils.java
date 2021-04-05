package com.ril.fabric.schema.util;

import com.jio.fabric.commons.*;
import com.ril.fabric.schema.domain.QuantityTemplate;

public class QuantityUtils {

    public static FabricQuantity getQuantitySchema(QuantityTemplate quantityTemplate) {

        FabricQuantity.Builder quantityBuilder = FabricQuantity.newBuilder();
        quantityBuilder.setType(quantityTemplate.getType());
        quantityBuilder.setUnit(quantityTemplate.getUnit());

        if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyNumeric))
            setNumericSubType(quantityTemplate.getQuantitySubType(), quantityBuilder);
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtySymbolic))
            quantityBuilder.setSymbolic(FabricQtySymbolic.newBuilder().build());
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyTemporal))
            setTemporalSubType(quantityTemplate.getQuantitySubType(), quantityBuilder);
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtySpatial))
            quantityBuilder.setSpatial(FabricQtySpatial.newBuilder().build());
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyDemographic))
            quantityBuilder.setDemographic(FabricQtyDemographic.newBuilder().build());
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyMonetary))
            quantityBuilder.setMonetary(FabricQtyMonetary.newBuilder().build());

        return quantityBuilder.build();

    }

    private static void setTemporalSubType(QuantityTemplate.JfQuantitySubType quantitySubType, FabricQuantity.Builder quantityBuilder) {
        switch (quantitySubType) {
            case timestamp:
                quantityBuilder.setTemporal(FabricQtyTemporal.newBuilder().setTimestamp(FabricQtyTimestamp.newBuilder().build()).build());
            case date:
                quantityBuilder.setTemporal(FabricQtyTemporal.newBuilder().setDate(FabricQtyDate.newBuilder().build()).build());
            case time:
                quantityBuilder.setTemporal(FabricQtyTemporal.newBuilder().setTime(FabricQtyTime.newBuilder()).build());
            case date_time:
                quantityBuilder.setTemporal(FabricQtyTemporal.newBuilder().setDateTime(FabricQtyDateTime.newBuilder()).build());
            case date_range:
                quantityBuilder.setTemporal(FabricQtyTemporal.newBuilder().setDateRange(FabricQtyDateRange.newBuilder()).build());
            case time_range:
                quantityBuilder.setTemporal(FabricQtyTemporal.newBuilder().setTimeRange(FabricQtyTimeRange.newBuilder()).build());
            case duration:
                quantityBuilder.setTemporal(FabricQtyTemporal.newBuilder().setDuration(FabricQtyDuration.newBuilder().build()).build());
        }
    }

    private static void setNumericSubType(QuantityTemplate.JfQuantitySubType quantitySubType, FabricQuantity.Builder quantityBuilder) {
        switch (quantitySubType) {
            case float_value:
                quantityBuilder.setNumeric(FabricQtyNumeric.newBuilder().setFloatValue(0).build());
            case double_value:
                quantityBuilder.setNumeric(FabricQtyNumeric.newBuilder().setDoubleValue(0).build());
            case sint32_value:
                quantityBuilder.setNumeric(FabricQtyNumeric.newBuilder().setSint32Value(0).build());
            case sint64_value:
                quantityBuilder.setNumeric(FabricQtyNumeric.newBuilder().setSint64Value(0).build());
            case uint32_value:
                quantityBuilder.setNumeric(FabricQtyNumeric.newBuilder().setUint32Value(0).build());
            case uint64_value:
                quantityBuilder.setNumeric(FabricQtyNumeric.newBuilder().setUint64Value(0).build());
        }
    }
}
