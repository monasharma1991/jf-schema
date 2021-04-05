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
        ResponseEntity<?> response = restTemplate.postForEntity("/schema?name=schema-test", null, Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void getErrorWhileCreating() {
        ResponseEntity<?> response = restTemplate.postForEntity("/schema/log?vertical=a&source=&domain=c", null, Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void setEntityToJFLogSchema() {
        restTemplate.put("/schema/entity/1056666306?type=value", Arrays.asList("customer"), Void.class);
    }

    @Test
    public void setPropertyToJFLogSchema() {
        QuantityTemplate quantityTemplate = new QuantityTemplate();
        quantityTemplate.setPropertyName("invoice_total");
        quantityTemplate.setType("type");
        quantityTemplate.setUnit("unit");
        quantityTemplate.setQuantityType(QuantityTemplate.JfQuantityType.QtyNumeric);
        quantityTemplate.setQuantitySubType(QuantityTemplate.JfQuantitySubType.float_value);
        restTemplate.put("/schema/property/1056666306?type=value", quantityTemplate);
    }

    @Test
    public void setAttributeNameToJFLogSchema() {
        QuantityTemplate quantityTemplate = new QuantityTemplate();
        quantityTemplate.setPropertyName("name");
        quantityTemplate.setType("type");
        quantityTemplate.setUnit("unit");
        quantityTemplate.setQuantityType(QuantityTemplate.JfQuantityType.QtySymbolic);
        restTemplate.put("/schema/attribute/1056666306?entityType=customer&type=value", quantityTemplate);  // entity does not exist error
    }

    @Test
    public void setAttributeAgeToJFLogSchema() {
        QuantityTemplate quantityTemplate = new QuantityTemplate();
        quantityTemplate.setPropertyName("age");
        quantityTemplate.setType("type");
        quantityTemplate.setUnit("unit");
        quantityTemplate.setQuantityType(QuantityTemplate.JfQuantityType.QtyNumeric);
        quantityTemplate.setQuantitySubType(QuantityTemplate.JfQuantitySubType.sint32_value);
        restTemplate.put("/schema/attribute/1056666306?entityType=cell&type=value", quantityTemplate);  // entity does not exist error
    }

    @Test
    public void getLogSchemaById() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/schema/log/6062cf1e6d624228c31e9815", String.class);
        System.out.println(responseEntity.getBody());
    }
}
