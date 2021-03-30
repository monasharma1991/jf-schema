package com.ril.fabric.schema.impl;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jio.commons.*;
import com.jio.fabric.schema.*;
import com.ril.fabric.schema.dao.MongoTemplateService;
import com.ril.fabric.schema.domain.EventSchemaType;
import com.ril.fabric.schema.domain.QuantityTemplate;
import com.ril.fabric.schema.interfaces.AttributeStoreSchemaInterface;
import org.bson.Document;
import org.bson.types.ObjectId;
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
    public ResponseEntity<?> addAttributeToSchema(String logSchemaId, String entityType, QuantityTemplate quantityTemplate) {

        Document document = mongoTemplateService.findById(logSchemaId, EventSchemaType.getLogSchemaCollection());
        if (document == null) {
            String[] arguments = {"logSchema", logSchemaId};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.msg", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }
        document.remove("_id");
        FabricEventSchema.Builder eventSchemaBuilder = FabricEventSchema.newBuilder();

        try {
            JsonFormat.parser().merge(document.toJson(), eventSchemaBuilder);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        FabricEntityStoreSchema entityStoreSchema = eventSchemaBuilder.getLogEventSchema().getEntitiesSchema();
        if (!entityStoreSchema.getKeysList().contains(entityType)) {
            String[] arguments = {"entity", entityType};
            return new ResponseEntity<>(msgSrc.getMessage("db.notFound.msg", arguments, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        FabricAttributeStoreSchema.Builder attributeStoreSchema = eventSchemaBuilder.getLogEventSchema().getAttributesSchema().toBuilder();

        FabricQuantityStoreSchema.Builder propertyBuilder = FabricQuantityStoreSchema.newBuilder();
        if (attributeStoreSchema.getValuesMap().get(entityType) != null) {
            propertyBuilder = attributeStoreSchema.getValuesMap().get(entityType).toBuilder();
        }

        String propertyName = quantityTemplate.getPropertyName();
        propertyBuilder.addKeys(propertyName);
        propertyBuilder.putValues(propertyName, getQuantitySchema(quantityTemplate));

        attributeStoreSchema.putValues(entityType, propertyBuilder.build());
        eventSchemaBuilder.getLogEventSchemaBuilder().setAttributesSchema(attributeStoreSchema.build());

        try {
            Document documentNew = Document.parse(JsonFormat.printer().print(eventSchemaBuilder));
            documentNew.put("_id", new ObjectId(logSchemaId));
            mongoTemplateService.saveDocument(documentNew, EventSchemaType.getLogSchemaCollection());
            return new ResponseEntity<>(documentNew.toJson(), HttpStatus.OK);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(msgSrc.getMessage("proto.parse.exc", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private FabricQuantitySchema getQuantitySchema(QuantityTemplate quantityTemplate) {

        FabricQuantitySchema.Builder quantityBuilder = FabricQuantitySchema.newBuilder();
        quantityBuilder.setType(quantityTemplate.getType());
        quantityBuilder.setUnit(quantityTemplate.getUnit());
        Quantity.Builder qtyBuilder = Quantity.newBuilder();

        if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyNumeric))
            setNumericSubType(quantityTemplate.getQuantitySubType(), qtyBuilder);
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtySymbolic))
            qtyBuilder.setSymbolic(QtySymbolic.newBuilder().build());
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyTemporal))
            setTemporalSubType(quantityTemplate.getQuantitySubType(), qtyBuilder);
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtySpatial))
            qtyBuilder.setSpatial(QtySpatial.newBuilder().build());
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyDemographic))
            qtyBuilder.setDemographic(QtyDemographic.newBuilder().build());
        else if (quantityTemplate.getQuantityType().equals(QuantityTemplate.JfQuantityType.QtyMonetary))
            qtyBuilder.setMonetary(QtyMonetary.newBuilder().build());

        quantityBuilder.setQuantityTemplate(qtyBuilder.build());
        return quantityBuilder.build();

    }

    private void setTemporalSubType(QuantityTemplate.JfQuantitySubType quantitySubType, Quantity.Builder quantityBuilder) {
        switch (quantitySubType) {
            case timestamp:
                quantityBuilder.setTemporal(QtyTemporal.newBuilder().setTimestamp(QtyTimestamp.newBuilder().build()).build());
            case date:
                quantityBuilder.setTemporal(QtyTemporal.newBuilder().setDate(QtyDate.newBuilder().build()).build());
            case time:
                quantityBuilder.setTemporal(QtyTemporal.newBuilder().setTime(QtyTime.newBuilder()).build());
            case date_time:
                quantityBuilder.setTemporal(QtyTemporal.newBuilder().setDateTime(QtyDateTime.newBuilder()).build());
            case date_range:
                quantityBuilder.setTemporal(QtyTemporal.newBuilder().setDateRange(QtyDateRange.newBuilder()).build());
            case time_range:
                quantityBuilder.setTemporal(QtyTemporal.newBuilder().setTimeRange(QtyTimeRange.newBuilder()).build());
            case duration:
                quantityBuilder.setTemporal(QtyTemporal.newBuilder().setDuration(QtyDuration.newBuilder().build()).build());
        }
    }

    private void setNumericSubType(QuantityTemplate.JfQuantitySubType quantitySubType, Quantity.Builder quantityBuilder) {
        switch (quantitySubType) {
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
    }
}
