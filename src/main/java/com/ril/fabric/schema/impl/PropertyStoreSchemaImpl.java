package com.ril.fabric.schema.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.jio.protos.commons.QtyNumeric;
import com.jio.protos.commons.QtySymbolic;
import com.jio.protos.commons.Quantity;
import com.jio.protos.fabric.event.LogEventSchema;
import com.jio.protos.fabric.store.PropertyStoreSchema;
import com.ril.fabric.schema.dao.MongoTemplateService;
import com.ril.fabric.schema.domain.EventSchemaType;
import com.ril.fabric.schema.domain.QuantityTemplate;
import com.ril.fabric.schema.exception.ExceptionResponse;
import com.ril.fabric.schema.interfaces.PropertyStoreSchemaInterface;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PropertyStoreSchemaImpl implements PropertyStoreSchemaInterface {


    @Autowired
    private MongoTemplateService mongoTemplateService;

    @Override
    public ResponseEntity<?> addPropertyToSchema(String logSchemaId, QuantityTemplate quantityTemplate) {

        Document document = mongoTemplateService.findById(logSchemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null)
            return new ResponseEntity<>(new ExceptionResponse(new Date(), "No record found for schema id: " + logSchemaId, ""), HttpStatus.NOT_FOUND);

        document.remove("_id");
        LogEventSchema.JFLogEventSchema.Builder logEventSchemaBuilder = LogEventSchema.JFLogEventSchema.newBuilder();
        try {
            JsonFormat.parser().merge(document.toJson(), logEventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(new ExceptionResponse(new Date(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PropertyStoreSchema.JFPropertyStoreSchema.Builder propertyStoreSchema = logEventSchemaBuilder.getPropertiesSchemaBuilder();
        String propertyName = quantityTemplate.getPropertyName();
        if (propertyStoreSchema.getKeysList().contains(propertyName))
            return new ResponseEntity<>("Already exists", HttpStatus.OK);

        propertyStoreSchema.addKeys(propertyName);
        propertyStoreSchema.putValues(propertyName, getQuantitySchema(quantityTemplate));

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(logEventSchemaBuilder));
            documentNew.put("_id", new ObjectId(logSchemaId));
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew, HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(new ExceptionResponse(new Date(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PropertyStoreSchema.JFQuantitySchema getQuantitySchema(QuantityTemplate quantityTemplate) {

        PropertyStoreSchema.JFQuantitySchema.Builder propertyBuilder = PropertyStoreSchema.JFQuantitySchema.newBuilder();
        propertyBuilder.setType(quantityTemplate.getType());
        propertyBuilder.setUnit(quantityTemplate.getUnit());
        Quantity.Builder quantityBuilder = Quantity.newBuilder();

        if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyNumeric)) {
            switch (quantityTemplate.getQuantitySubType()) {
                case float_value:
                    quantityBuilder.setNumeric(QtyNumeric.newBuilder().setFloatValue(0).build());
                case double_value:
                    quantityBuilder.setNumeric(QtyNumeric.newBuilder().setDoubleValue(0).build());
                case sint32_value:
                    quantityBuilder.setNumeric(QtyNumeric.newBuilder().setSint32Value(0).build());
                case sint64_value:
                    quantityBuilder.setNumeric(QtyNumeric.newBuilder().setSint64Value(0).build());
                case uint32_value:
                    quantityBuilder.setNumeric(QtyNumeric.newBuilder().setUint32Value(0).build());
                case uint64_value:
                    quantityBuilder.setNumeric(QtyNumeric.newBuilder().setUint64Value(0).build());
            }
        } else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyNumeric))
            quantityBuilder.setSymbolic(QtySymbolic.newBuilder().build());


        propertyBuilder.setQuantityTemplate(quantityBuilder.build());
        return propertyBuilder.build();

    }
}