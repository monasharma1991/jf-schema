package com.ril.fabric.schema.impl;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jio.fabric.event.FabricEventSchema;
import com.ril.fabric.schema.dao.MongoTemplateService;
import com.ril.fabric.schema.domain.EventSchemaType;
import com.ril.fabric.schema.exception.ExceptionResponse;
import com.ril.fabric.schema.interfaces.EventSchemaInterface;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;


@Service
@Slf4j
public class EventSchemaImpl implements EventSchemaInterface {

    @Autowired
    private MongoTemplateService mongoTemplateService;
    @Autowired
    private MessageSource msgSrc;

    @Override
    public ResponseEntity<?> createEventSchema(String name) {

        FabricEventSchema jfLogEventSchema = FabricEventSchema.newBuilder()
                .setSchemaName(name)
                .build();

        String schemaJson = null;
        try {
            schemaJson = JsonFormat.printer().print(jfLogEventSchema);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Document doc = Document.parse(schemaJson);
        doc.put("_id", Math.abs(name.hashCode()));
        mongoTemplateService.saveDocument(doc, EventSchemaType.getLogSchemaCollection());
        return new ResponseEntity<>(doc.toJson(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> setFinaliseAndIndex(int eventSchemaId) {
        Document document = mongoTemplateService.findById(eventSchemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null)
            return new ResponseEntity<>(new ExceptionResponse(new Date(), "No LogEventSchema found for schema id: " + eventSchemaId, ""), HttpStatus.NOT_FOUND);

        document.remove("_id");
        FabricEventSchema.Builder logEventSchemaBuilder = FabricEventSchema.newBuilder();
        try {
            JsonFormat.parser().merge(document.toJson(), logEventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //TODO-call a method which set finalise in logEventSchema
        //TODO- call a method to update a index in logEventSchema

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(logEventSchemaBuilder));
            documentNew.put("_id", eventSchemaId);
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew.toJson(), HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Override
    public ResponseEntity<?> getSchemaDocumentById(int eventSchemaId) {

        Document document = mongoTemplateService.findById(eventSchemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null) {
            String[] arguments = {"logSchema", "" + eventSchemaId};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.msg", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(document.toJson(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getSchemaProtoById(int eventSchemaId) {

        Document document = mongoTemplateService.findById(eventSchemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null) {
            String[] arguments = {"logSchema", ""+eventSchemaId};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.msg", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        document.remove("_id");
        FabricEventSchema.Builder logEventSchemaBuilder = FabricEventSchema.newBuilder();
        try {
            JsonFormat.parser().merge(document.toJson(), logEventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(logEventSchemaBuilder.build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllSchemas() {
        List<Document> result = mongoTemplateService.findAll(EventSchemaType.getLogSchemaCollection());
        if (result.isEmpty())
            return new ResponseEntity<>("No records found", HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
