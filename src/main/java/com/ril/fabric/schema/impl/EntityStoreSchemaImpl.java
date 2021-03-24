package com.ril.fabric.schema.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.jio.protos.fabric.event.LogEventSchema;
import com.jio.protos.fabric.store.EntityStoreSchema.JFEntityStoreSchema;
import com.ril.fabric.schema.dao.MongoTemplateService;
import com.ril.fabric.schema.domain.EventSchemaType;
import com.ril.fabric.schema.exception.ExceptionResponse;
import com.ril.fabric.schema.interfaces.EntityStoreSchemaInterface;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class EntityStoreSchemaImpl implements EntityStoreSchemaInterface {

    @Autowired
    private MongoTemplateService mongoTemplateService;

    @Override
    public ResponseEntity<?> addEntitiesToLogSchema(String schemaId, Set<String> entities) {

        Document document = mongoTemplateService.findById(schemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null)
            return new ResponseEntity<>(new ExceptionResponse(new Date(), "No record found for schema id: " + schemaId, ""), HttpStatus.NOT_FOUND);

        document.remove("_id");
        LogEventSchema.JFLogEventSchema.Builder logEventSchemaBuilder = LogEventSchema.JFLogEventSchema.newBuilder();
        try {
            getEventBuilder(document.toJson(), logEventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(new ExceptionResponse(new Date(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logEventSchemaBuilder.setEntitiesSchema(JFEntityStoreSchema.newBuilder().addAllKeys(entities).build());

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(logEventSchemaBuilder));
            documentNew.put("_id", new ObjectId(schemaId));
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew, HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(new ExceptionResponse(new Date(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void getEventBuilder(String json, Message.Builder builder) throws InvalidProtocolBufferException {
        JsonFormat.parser().merge(json, builder);
    }

    @Override
    public ResponseEntity<?> addEntityToLogSchema(String schemaId, String entity) {

        Document document = mongoTemplateService.findById(schemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null)
            return new ResponseEntity<>(new ExceptionResponse(new Date(), "No entity found for schema id: " + schemaId, ""), HttpStatus.NOT_FOUND);

        document.remove("_id");
        LogEventSchema.JFLogEventSchema.Builder logEventSchemaBuilder = LogEventSchema.JFLogEventSchema.newBuilder();
        try {
            JsonFormat.parser().merge(document.toJson(), logEventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(new ExceptionResponse(new Date(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        boolean hasKey = hasKey(entity, logEventSchemaBuilder.getEntitiesSchema());
        JFEntityStoreSchema.Builder jfEntityStoreSchemaBuilder = logEventSchemaBuilder.getEntitiesSchema().toBuilder();
        jfEntityStoreSchemaBuilder.addKeys(entity);
        logEventSchemaBuilder.setEntitiesSchema(jfEntityStoreSchemaBuilder.build());

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(logEventSchemaBuilder));
            documentNew.put("_id", new ObjectId(schemaId));
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew, HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(new ExceptionResponse(new Date(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> searchByEntityType(String phrase) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(phrase, "i"));
        List<Document> documents = mongoTemplateService.findByQuery(query, EventSchemaType.getLogSchemaCollection());

        if (documents.isEmpty())
            return new ResponseEntity<>(new ExceptionResponse(new Date(), "No records found", ""), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(documents, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> addEntitiesToPivotSchema(String pivotSchemaId, Set<String> entities) {
        return null;
    }

    private boolean hasKey(String entity, JFEntityStoreSchema entitiesSchema) {

        if (entitiesSchema.getKeysList().contains(entity))
            return true;
        else
            return false;
    }
}
