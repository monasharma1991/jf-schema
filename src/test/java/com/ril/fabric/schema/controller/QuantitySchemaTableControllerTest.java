package com.ril.fabric.schema.controller;

import com.jio.fabric.FabricQuantityUnit;
import com.ril.fabric.schema.domain.NumericSchema;
import com.ril.fabric.schema.domain.SymbolicSchema;
import com.ril.fabric.schema.domain.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuantitySchemaTableControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void createNumericQuantitySchema(){
        NumericSchema numericSchema = new NumericSchema();
        numericSchema.setVertical("TestVertical");
        numericSchema.setDomain("TestDomain");
        numericSchema.setGroup("TestGroup");
        numericSchema.setName("First");
        numericSchema.setUnit(Unit.CM);
        numericSchema.setMinValue(10);
        numericSchema.setMaxValue(99);

        ResponseEntity<?> response = restTemplate.postForEntity("/quantitySchema/numeric", numericSchema, Object.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createNumericQuantitySchemaFail(){
        NumericSchema numericSchema = new NumericSchema();
        numericSchema.setVertical("TestVertical");
        numericSchema.setDomain("TestDomain");
        numericSchema.setGroup("TestGroup");
        numericSchema.setName("First");
        numericSchema.setUnit(Unit.CELSIUS);
        numericSchema.setMinValue(10);
        numericSchema.setMaxValue(99);

        ResponseEntity<?> response = restTemplate.postForEntity("/quantitySchema/numeric", numericSchema, Object.class);
//        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createSymbolicSchemaTest(){
        SymbolicSchema symbolicSchema = new SymbolicSchema();
        symbolicSchema.setVertical("TestVertical");
        symbolicSchema.setDomain("TestDomain");
        symbolicSchema.setGroup("TestGroup");
        symbolicSchema.setName("second");
        symbolicSchema.setUnit(Unit.KG);
        symbolicSchema.getKeyList().add("first");
        symbolicSchema.getKeyList().add("second");
        symbolicSchema.getKeyList().add("third");
        symbolicSchema.getKeyList().add("forth");

        ResponseEntity<?> response = restTemplate.postForEntity("/quantitySchema/symbolic", symbolicSchema, Object.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
}
