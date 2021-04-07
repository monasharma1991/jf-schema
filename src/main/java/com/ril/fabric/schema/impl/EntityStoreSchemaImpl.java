package com.ril.fabric.schema.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jio.fabric.FabricIndex;
import com.jio.fabric.FabricEventSchema;
import com.ril.fabric.schema.dao.MongoTemplateService;
import com.ril.fabric.schema.domain.EventSchemaType;
import com.ril.fabric.schema.interfaces.EntityStoreSchemaInterface;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class EntityStoreSchemaImpl implements EntityStoreSchemaInterface {

    @Autowired
    private MongoTemplateService mongoTemplateService;
    @Autowired
    private MessageSource msgSrc;

    @Override
    public ResponseEntity<?> addEntitiesToSchema(int schemaId, Set<String> entities, String type) {

        Document document = mongoTemplateService.findById(schemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null) {
            String[] arguments = {"logSchema", "" + schemaId};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.msg", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        document.remove("_id");
        FabricEventSchema.Builder eventSchemaBuilder = FabricEventSchema.newBuilder();
        try {
            JsonFormat.parser().merge(document.toJson(), eventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (type.equals("key"))
            eventSchemaBuilder.getKeySchemaBuilder().setEntityType(FabricIndex.newBuilder().addAllKey(entities).build());

        else if (type.equals("value"))
            eventSchemaBuilder.getValueSchemaBuilder().setEntityType(FabricIndex.newBuilder().addAllKey(entities).build());

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(eventSchemaBuilder));
            documentNew.put("_id", schemaId);
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew.toJson(), HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> addEntityToSchema(int schemaId, String entity, String type) {

        Document document = mongoTemplateService.findById(schemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null) {
            String[] arguments = {"schema", "" + schemaId};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.msg", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        document.remove("_id");
        FabricEventSchema.Builder eventSchemaBuilder = FabricEventSchema.newBuilder();
        try {
            JsonFormat.parser().merge(document.toJson(), eventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (type.equals("key"))
            eventSchemaBuilder.getKeySchema().getEntityType().toBuilder().addKey(entity);
        if (type.equals("value"))
            eventSchemaBuilder.getValueSchema().getEntityType().toBuilder().addKey(entity);

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(eventSchemaBuilder));
            documentNew.put("_id", schemaId);
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew.toJson(), HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> searchByEntityType(String phrase) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(phrase, "i"));
        List<Document> documents = mongoTemplateService.findByQuery(query, EventSchemaType.getLogSchemaCollection());

        if (documents.isEmpty()) {
            String[] arguments = {phrase};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.phrase", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

}
