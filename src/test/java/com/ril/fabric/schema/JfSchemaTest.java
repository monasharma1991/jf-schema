package com.ril.fabric.schema;

import com.jio.fabric.commons.FabricQtyNumeric;
import com.jio.fabric.commons.FabricQuantity;
import org.junit.jupiter.api.Test;

public class JfSchemaTest {


    @Test
    public void createJfSchema(){

    }

    @Test
    public void prototemplateTest(){
        FabricQuantity.Builder quantityBuilder = FabricQuantity.newBuilder();
        quantityBuilder.setNumeric(FabricQtyNumeric.newBuilder().setFloatValue(0).build());
        FabricQuantity quantity = quantityBuilder.build();

        if (quantity.hasNumeric()){
            System.out.println(" Yes it is numeric");
            FabricQtyNumeric qtyNumeric = quantity.getNumeric();
            if (qtyNumeric.getValueIsOneOfCase().equals(FabricQtyNumeric.ValueIsOneOfCase.FLOAT_VALUE))
                System.out.println(" Yes it is float");
            if (qtyNumeric.getValueIsOneOfCase().equals(FabricQtyNumeric.ValueIsOneOfCase.SINT32_VALUE))
                System.out.println(" Yes it is sint32");

        }
    }
}
