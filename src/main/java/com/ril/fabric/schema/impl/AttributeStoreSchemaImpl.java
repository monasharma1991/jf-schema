package com.ril.fabric.schema.impl;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jio.fabric.FabricEventSchema;
import com.jio.fabric.FabricIndex;
import com.jio.fabric.FabricStoreSchema;
import com.ril.fabric.schema.dao.MongoTemplateService;
import com.ril.fabric.schema.domain.EventSchemaType;
import com.ril.fabric.schema.domain.QuantityTemplate;
import com.ril.fabric.schema.interfaces.AttributeStoreSchemaInterface;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AttributeStoreSchemaImpl implements AttributeStoreSchemaInterface {

    @Autowired
    private MongoTemplateService mongoTemplateService;
    @Autowired
    private MessageSource msgSrc;

    @Override
    public ResponseEntity<?> addAttributeToSchema(int schemaId, String entityType, String type, QuantityTemplate quantityTemplate) {

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

        FabricStoreSchema.Builder storeSchema = null;
        if (type.equals("key"))
            storeSchema = eventSchemaBuilder.getKeySchemaBuilder();
        else if (type.equals("value"))
            storeSchema = eventSchemaBuilder.getValueSchemaBuilder();

        if (!storeSchema.getEntityType().getKeyList().contains(entityType)) {
            String[] arguments = {"entity", entityType};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.msg", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

/*        FabricIndex fabricIndex = storeSchema.getAttributeMap().get(entityType);
        if (fabricIndex == null)
            fabricIndex = FabricIndex.newBuilder().build();*/

        String propertyName = quantityTemplate.getPropertyName();
        if (storeSchema.getAttributeMap().get(entityType) == null) {
            FabricIndex fabricIndex = FabricIndex.newBuilder().addKey(propertyName).build();
            storeSchema.putAttribute(entityType, fabricIndex);
        } else {
            FabricIndex.Builder fabricIndex = storeSchema.getAttributeMap().get(entityType).toBuilder();
            fabricIndex.addKey(propertyName);
            storeSchema.putAttribute(entityType, fabricIndex.build());
        }

      //  FabricQuantity fabricQuantity = QuantityUtils.getQuantitySchema(quantityTemplate);
        String quantityType = ""; //get it from FabricQuantitySchemaTable
        storeSchema.putTemplate(propertyName, quantityType);

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(eventSchemaBuilder));
            documentNew.put("_id", schemaId);
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew.toJson(), HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
