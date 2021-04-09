package com.ril.fabric.schema.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jio.fabric.*;

import com.ril.fabric.schema.dao.MongoTemplateService;
import com.ril.fabric.schema.domain.EventSchemaType;
import com.ril.fabric.schema.domain.NumericSchema;
import com.ril.fabric.schema.domain.SymbolicSchema;
import com.ril.fabric.schema.interfaces.QuantitySchemaTableInterface;
import lombok.extern.slf4j.Slf4j;
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

@Service
@Slf4j
public class QuantitySchemaTableImpl implements QuantitySchemaTableInterface {

    @Autowired
    private MongoTemplateService mongoTemplateService;
    @Autowired
    private MessageSource msgSrc;

    @Override
    public ResponseEntity<?> createNumericQuantitySchemaTable(NumericSchema numericSchema) {

        boolean nameAlreadyExist = isNameAlreadyExist(numericSchema.getName());
        if (nameAlreadyExist)
            return new ResponseEntity<>("name is already exist."+numericSchema.getName(), HttpStatus.BAD_REQUEST);

        FabricQuantityNumeric minimum = FabricQuantityNumeric.newBuilder()
                .setSint32Value(numericSchema.getMinValue())
                .build();

        FabricQuantityNumeric maximum = FabricQuantityNumeric.newBuilder()
                .setUint32Value(numericSchema.getMaxValue())
                .build();

        FabricNumericQuantitySchema fabricNumericQuantitySchema = FabricNumericQuantitySchema.newBuilder()
                .setMinimum(minimum)
                .setMaximum(maximum)
                .build();


        FabricQuantitySchema fabricQuantitySchema = FabricQuantitySchema.newBuilder()
                .setVertical(numericSchema.getVertical())
                .setDomain(numericSchema.getDomain())
                .setGroup(numericSchema.getGroup())
                .setName(numericSchema.getName())
                .setUnit(FabricQuantityUnit.valueOf(numericSchema.getUnit().toString()))
                .setNumeric(fabricNumericQuantitySchema)
                .build();

        String schemaJson = null;
        try {
            schemaJson = JsonFormat.printer().print(fabricQuantitySchema);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Document doc = Document.parse(schemaJson);
        doc.put("_id", Math.abs(numericSchema.getName().hashCode()));

        mongoTemplateService.saveDocument(doc, EventSchemaType.getFabricQuantitySchema());
        return new ResponseEntity<>(doc.toJson(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> createSymbolicQuantitySchemaTable(SymbolicSchema symbolicSchema) {

        boolean nameAlreadyExist = isNameAlreadyExist(symbolicSchema.getName());
        if (nameAlreadyExist)
            return new ResponseEntity<>("name is already exist."+symbolicSchema.getName(), HttpStatus.BAD_REQUEST);

        FabricIndex fabricIndex = FabricIndex.newBuilder()
                .addAllKey(symbolicSchema.getKeyList())
                .build();

        FabricSymbolicQuantitySchema fabricSymbolicQuantitySchema = FabricSymbolicQuantitySchema.newBuilder()
                .setDictionary(fabricIndex)
                .build();

        FabricQuantitySchema fabricQuantitySchema = FabricQuantitySchema.newBuilder()
                .setVertical(symbolicSchema.getVertical())
                .setDomain(symbolicSchema.getDomain())
                .setGroup(symbolicSchema.getGroup())
                .setName(symbolicSchema.getName())
                .setUnit(FabricQuantityUnit.valueOf(symbolicSchema.getUnit().toString()))
                .setSymbolic(fabricSymbolicQuantitySchema)
                .build();


        String schemaJson = null;
        try {
            schemaJson = JsonFormat.printer().print(fabricQuantitySchema);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Document doc = Document.parse(schemaJson);
        doc.put("_id", Math.abs(symbolicSchema.getName().hashCode()));

        mongoTemplateService.saveDocument(doc, EventSchemaType.getFabricQuantitySchema());
        return new ResponseEntity<>(doc.toJson(), HttpStatus.CREATED);
    }

    private Boolean isNameAlreadyExist(String name){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        List<Document> documents = mongoTemplateService.findByQuery(query, EventSchemaType.getFabricQuantitySchema());
        if ( !documents.isEmpty() && documents != null)
            return true;

        return false;
    }
}
