package com.ril.fabric.schema.impl;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jio.protos.fabric.event.LogEventSchema;
import com.jio.protos.fabric.event.LogEventSchema.JFLogEventSchema;
import com.ril.fabric.schema.dao.MongoTemplateService;
import com.ril.fabric.schema.domain.EventSchemaType;
import com.ril.fabric.schema.exception.ExceptionResponse;
import com.ril.fabric.schema.interfaces.LogEventSchemaInterface;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class LogEventSchemaImpl implements LogEventSchemaInterface {

    @Autowired
    MongoTemplateService mongoTemplateService;

    @Override
    public ResponseEntity<?> createLogEventSchema(String vertical, String source, String domain) {

        JFLogEventSchema jfLogEventSchema = JFLogEventSchema.newBuilder()
                .setVertical(vertical)
                .setSource(source)
                .setDomain(domain)
                .build();

        String schemaJson = null;
        try {
            schemaJson = JsonFormat.printer().print(jfLogEventSchema);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(new ExceptionResponse(new Date(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Document doc = Document.parse(schemaJson);
        mongoTemplateService.saveDocument(doc, EventSchemaType.getLogSchemaCollection());
        return new ResponseEntity<>(doc, HttpStatus.CREATED);
    }

    public ResponseEntity<?> createLogEventSchema_2(String vertical, String source, String domain) {
        JFLogEventSchema jfLogEventSchema = JFLogEventSchema.newBuilder()
                .setVertical(vertical)
                .setSource(source)
                .setDomain(domain)
                .build();
        String schemaJson = null;
        try {
            schemaJson = JsonFormat.printer().print(jfLogEventSchema);
        } catch (InvalidProtocolBufferException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        Document doc = Document.parse(schemaJson);
        mongoTemplateService.saveDocument(doc, EventSchemaType.getLogSchemaCollection());
        return new ResponseEntity<>(doc, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> setFinaliseAndIndex(String logEventSchemaId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getSchemaDocumentById(String logEventSchemaId) {

        Document document = mongoTemplateService.findById(logEventSchemaId,  EventSchemaType.getLogSchemaCollection());
        if (document == null)
            return new ResponseEntity<>(new ExceptionResponse(new Date(), "No entity found for schema id: " + logEventSchemaId, ""), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(document, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getLogSchemaProtoById(String logEventSchemaId) {

        Document document = mongoTemplateService.findById(logEventSchemaId,  EventSchemaType.getLogSchemaCollection());
        if (document == null)
            return new ResponseEntity<>(new ExceptionResponse(new Date(), "No entity found for schema id: " + logEventSchemaId, ""), HttpStatus.NOT_FOUND);

        document.remove("_id");
        LogEventSchema.JFLogEventSchema.Builder logEventSchemaBuilder = LogEventSchema.JFLogEventSchema.newBuilder();
        try {
            JsonFormat.parser().merge(document.toJson(), logEventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(new ExceptionResponse(new Date(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(logEventSchemaBuilder.build(), HttpStatus.OK);
    }

}
