package com.ril.fabric.schema;

import com.google.protobuf.Descriptors;
import com.jio.protos.commons.QtyNumeric;
import com.jio.protos.commons.Quantity;
import org.junit.jupiter.api.Test;

public class JfSchemaTest {


    @Test
    public void createJfSchema(){

    }

    @Test
    public void prototemplateTest(){
        Quantity.Builder quantityBuilder = Quantity.newBuilder();
        quantityBuilder.setNumeric(QtyNumeric.newBuilder().setFloatValue(0).build());
        Quantity quantity = quantityBuilder.build();

        if (quantity.hasNumeric()){
            System.out.println(" Yes it is numeric");
            QtyNumeric qtyNumeric = quantity.getNumeric();
            if (qtyNumeric.getValueIsOneOfCase().equals(QtyNumeric.ValueIsOneOfCase.FLOAT_VALUE))
                System.out.println(" Yes it is float");
            if (qtyNumeric.getValueIsOneOfCase().equals(QtyNumeric.ValueIsOneOfCase.SINT32_VALUE))
                System.out.println(" Yes it is sint32");

        }
    }
}
