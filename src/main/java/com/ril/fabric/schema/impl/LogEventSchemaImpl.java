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
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


@Service
@Slf4j
public class LogEventSchemaImpl implements LogEventSchemaInterface {

    @Autowired
    private MongoTemplateService mongoTemplateService;
    @Autowired
    private MessageSource msgSrc;

    @Override
    public ResponseEntity<?> createLogEventSchema(String vertical, String source, String domain, String topic) {

        if ( vertical.isBlank() )
            return new ResponseEntity<>("Vertical is not null. Please provide!!!", HttpStatus.BAD_REQUEST);
        if ( source.isBlank() )
            return new ResponseEntity<>("Source is not null. Please provide!!!", HttpStatus.BAD_REQUEST);
        if ( domain.isBlank() )
            return new ResponseEntity<>("Domain is not null. Please provide!!!", HttpStatus.BAD_REQUEST);

        if ( topic.isBlank() || topic == null )
            topic = generateTopic(vertical, source, domain);

        JFLogEventSchema jfLogEventSchema = JFLogEventSchema.newBuilder()
                .setVertical(vertical)
                .setSource(source)
                .setDomain(domain)
                .setTopic(topic)
                .build();

        String schemaJson = null;
        try {
            schemaJson = JsonFormat.printer().print(jfLogEventSchema);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Document doc = Document.parse(schemaJson);
        mongoTemplateService.saveDocument(doc, EventSchemaType.getLogSchemaCollection());
        return new ResponseEntity<>(doc.toJson(), HttpStatus.CREATED);
    }
    private String generateTopic(String vertical, String source, String domain){
        return vertical+"_"+source+"_"+domain;
    }

    @Override
    public ResponseEntity<?> setFinaliseAndIndex(String logEventSchemaId) {
        Document document = mongoTemplateService.findById(logEventSchemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null)
            return new ResponseEntity<>(new ExceptionResponse(new Date(), "No LogEventSchema found for schema id: " + logEventSchemaId, ""), HttpStatus.NOT_FOUND);

        document.remove("_id");
        LogEventSchema.JFLogEventSchema.Builder logEventSchemaBuilder = LogEventSchema.JFLogEventSchema.newBuilder();
        try {
            JsonFormat.parser().merge(document.toJson(), logEventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //TODO-call a method which set finalise in logEventSchema
        //TODO- call a method to update a index in logEventSchema

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(logEventSchemaBuilder));
            documentNew.put("_id", new ObjectId(logEventSchemaId));
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew.toJson(), HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @Override
    public ResponseEntity<?> getSchemaDocumentById(String logEventSchemaId) {

        Document document = mongoTemplateService.findById(logEventSchemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null) {
            String[] arguments = {"logSchema", logEventSchemaId};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.msg", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(document.toJson(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getLogSchemaProtoById(String logEventSchemaId) {

        Document document = mongoTemplateService.findById(logEventSchemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null) {
            String[] arguments = {"logSchema", logEventSchemaId};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.msg", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        document.remove("_id");
        LogEventSchema.JFLogEventSchema.Builder logEventSchemaBuilder = LogEventSchema.JFLogEventSchema.newBuilder();
        try {
            JsonFormat.parser().merge(document.toJson(), logEventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(logEventSchemaBuilder.build(), HttpStatus.OK);
    }
}
