package com.ril.fabric.schema.controller;

import com.google.protobuf.util.JsonFormat;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AttributeStoreSchemaControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String eventId;

    @BeforeEach
    public void createEventSchema(){
        String name = "testEventSchema";

        ResponseEntity<?> responseEntity = restTemplate.postForEntity("/schema?name="+name, null, String.class);
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        Document document = Document.parse(responseEntity.getBody().toString());

        eventId = (String) document.get("_id");

    }

    @Test
    public void setFinaliseAndIndexTest(){

        ResponseEntity<?> response = restTemplate.postForEntity("/schema/"+eventId+"/save", null, Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getSchemaByIdTest(){
        ResponseEntity<?> response = restTemplate.getForEntity("/schema/"+eventId, Object.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getSchemaProtoByIdTest(){

    }
}
