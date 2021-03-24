package com.ril.fabric.schema;

import com.ril.fabric.schema.domain.QuantityTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchemaProtoTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createJFLogSchema() {
        restTemplate.postForEntity("/schema/log?vertical=a&source=b&domain=c", null, Void.class);
    }

    @Test
    public void setEntityToJFLogSchema() {
        restTemplate.put("/schema/entity/605b0ecb1545ac7be9ca9edf", Arrays.asList("customer","cell","grid"), Void.class);
    }

    @Test
    public void setPropertyToJFLogSchema() {
        QuantityTemplate quantityTemplate = new QuantityTemplate();
        quantityTemplate.setPropertyName("invoice_total");
        quantityTemplate.setType("type");
        quantityTemplate.setUnit("unit");
        quantityTemplate.setQuantityType(QuantityTemplate.JfQuantityType.QtyNumeric);
        quantityTemplate.setQuantitySubType(QuantityTemplate.JfQuantitySubType.float_value);

        restTemplate.put("/schema/property/log/605b0ecb1545ac7be9ca9edf",quantityTemplate);
    }
}
