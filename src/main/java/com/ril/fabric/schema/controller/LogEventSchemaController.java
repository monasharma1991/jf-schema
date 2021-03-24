package com.ril.fabric.schema.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ril.fabric.schema.interfaces.LogEventSchemaInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/schema/log")
public class LogEventSchemaController {

    private final LogEventSchemaInterface logEventSchemaInterface;

    @Autowired
    public LogEventSchemaController(LogEventSchemaInterface logEventSchemaInterface) {
        this.logEventSchemaInterface = logEventSchemaInterface;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<?> createLogEventSchema(@RequestParam String vertical, @RequestParam String source, @RequestParam String domain) throws InvalidProtocolBufferException {
        return logEventSchemaInterface.createLogEventSchema(vertical, source, domain);
    }

    @PutMapping("/{logEventSchemaId}/save")
    public ResponseEntity<?> save(@PathVariable String logEventSchemaId) {
        return logEventSchemaInterface.setFinaliseAndIndex(logEventSchemaId);
    }

    @GetMapping("/{logEventSchemaId}")
    public ResponseEntity<?> getLogSchemaById(@PathVariable String logEventSchemaId) {
        return logEventSchemaInterface.getSchemaDocumentById(logEventSchemaId);
    }

    /**
     * @param logEventSchemaId
     * @return JFLogEventSchema proto object
     * this api will be called by diferent services like filter , enrich to bootstap the schema instance
     */
    @GetMapping("/{logEventSchemaId}/proto")
    public ResponseEntity<?> getLogSchemaProtoById(@PathVariable String logEventSchemaId) {
        return logEventSchemaInterface.getLogSchemaProtoById(logEventSchemaId);
    }

}
