package com.ril.fabric.schema.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ril.fabric.schema.interfaces.EventSchemaInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/schema")
public class EventSchemaController {

    private final EventSchemaInterface eventSchemaInterface;

    @Autowired
    public EventSchemaController(EventSchemaInterface eventSchemaInterface) {
        this.eventSchemaInterface = eventSchemaInterface;
    }

    // TODO- add topic namre in req param if blank generate
    @PostMapping({"", "/"})
    public ResponseEntity<?> createEventSchema(@RequestParam String name) throws InvalidProtocolBufferException {
        return eventSchemaInterface.createEventSchema(name);
    }

    @PutMapping("/{schemaId}/save")
    public ResponseEntity<?> save(@PathVariable int schemaId) {
        return eventSchemaInterface.setFinaliseAndIndex(schemaId);
    }

    @GetMapping("/{eventSchemaId}")
    public ResponseEntity<?> getSchemaById(@PathVariable int eventSchemaId) {
        return eventSchemaInterface.getSchemaDocumentById(eventSchemaId);
    }

    /**
     * @param eventSchemaId
     * @return FabricEventSchema proto object
     * this api will be called by diferent services like filter , enrich to bootstap the schema instance
     */
    @GetMapping("/{eventSchemaId}/proto")
    public ResponseEntity<?> getSchemaProtoById(@PathVariable int eventSchemaId) {
        return eventSchemaInterface.getSchemaProtoById(eventSchemaId);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSchemas() {
        return eventSchemaInterface.getAllSchemas();
    }
}
