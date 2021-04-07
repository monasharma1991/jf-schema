package com.ril.fabric.schema.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jio.fabric.FabricEventSchema;
import com.jio.fabric.FabricStoreSchema;
import com.ril.fabric.schema.dao.MongoTemplateService;
import com.ril.fabric.schema.domain.EventSchemaType;
import com.ril.fabric.schema.domain.QuantityTemplate;
import com.ril.fabric.schema.interfaces.PropertyStoreSchemaInterface;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Service
@Slf4j
public class PropertyStoreSchemaImpl implements PropertyStoreSchemaInterface {


    @Autowired
    private MongoTemplateService mongoTemplateService;
    @Autowired
    private MessageSource msgSrc;

    @Override
    public ResponseEntity<?> addPropertyToSchema(int schemaId, QuantityTemplate quantityTemplate, String type) {
        // TODO - Validate QuantityTemplate
        List<String> errorList = validateQuantityTemplate(quantityTemplate);
        if (errorList != null && errorList.size() > 0) {
            log.info("Invalid data for Adding Property to Schema in QuantityTemplate. Please Provide !!!");
            return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
        }

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
            storeSchema = eventSchemaBuilder.getKeySchema().toBuilder();
        else if (type.equals("value"))
            storeSchema = eventSchemaBuilder.getValueSchema().toBuilder();

        String propertyName = quantityTemplate.getPropertyName();
        if (storeSchema.getProperty().getKeyList().contains(propertyName))
            return new ResponseEntity<>("Already exists", HttpStatus.INTERNAL_SERVER_ERROR);

        storeSchema.getPropertyBuilder().addKey(propertyName);
      //  FabricQuantity fabricQuantity = QuantityUtils.getQuantitySchema(quantityTemplate);
        String quantityType = ""; //get it from FabricQuantitySchemaTable
        storeSchema.putTemplate(propertyName, quantityType);

        if (type.equals("key"))
            eventSchemaBuilder.setKeySchema(storeSchema);
        else if (type.equals("value"))
            eventSchemaBuilder.setValueSchema(storeSchema);

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(eventSchemaBuilder));
            documentNew.put("_id", schemaId);
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew.toJson(), HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<String> validateQuantityTemplate(QuantityTemplate quantityTemplate) {
        List<String> errorList = new ArrayList<>();

        if (quantityTemplate.getPropertyName() == null)
            errorList.add("Property Name is not null in QuantityTemplate. Please Provide First !!!");
        if (quantityTemplate.getQuantityType() == null)
            errorList.add("Unit is not null in QuantityTemplate. Please Provide first !!!");

        return errorList;
    }

}
