package com.ril.fabric.schema;

import com.ril.fabric.schema.domain.QuantityTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchemaProtoTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createJFLogSchema() {
        ResponseEntity<?> response = restTemplate.postForEntity("/schema/log?vertical=a&source=b&domain=c&topic=", null, Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void getErrorWhileCreating(){
        ResponseEntity<?> response = restTemplate.postForEntity("/schema/log?vertical=a&source=&domain=c", null, Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void setEntityToJFLogSchema() {
        restTemplate.put("/schema/entity/6062cf1e6d624228c31e9815", Arrays.asList("customer", "cell"), Void.class);
    }

    @Test
    public void setPropertyToJFLogSchema() {
        QuantityTemplate quantityTemplate = new QuantityTemplate();
        quantityTemplate.setPropertyName("invoice_total");
        quantityTemplate.setType("type");
        quantityTemplate.setUnit("unit");
        quantityTemplate.setQuantityType(QuantityTemplate.JfQuantityType.QtyNumeric);
        quantityTemplate.setQuantitySubType(QuantityTemplate.JfQuantitySubType.float_value);
        restTemplate.put("/schema/property/log/6062cf1e6d624228c31e9815", quantityTemplate);
    }

    @Test
    public void setAttributesToJFLogSchema() {
        QuantityTemplate quantityTemplate = new QuantityTemplate();
        quantityTemplate.setPropertyName("name");
        quantityTemplate.setType("type");
        quantityTemplate.setUnit("unit");
        quantityTemplate.setQuantityType(QuantityTemplate.JfQuantityType.QtySymbolic);
        restTemplate.put("/schema/attribute/log/6062cf1e6d624228c31e9815?entityType=customer", quantityTemplate);  // entity does not exist error
    }
}
