package com.ril.fabric.schema.dao;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoTemplateService {

    @Autowired
    MongoTemplate mongoTemplate;


    public Document findById(int schemaId, String schemaCollection) {
        Document document = mongoTemplate.findById(schemaId, Document.class, schemaCollection);
        return document;
    }

    public void saveDocument(Document documentNew, String schemaCollection) {
        mongoTemplate.save(documentNew, schemaCollection);
    }

    public List<Document> findByQuery(Query query, String schemaCollection) {
        List<Document> documents = mongoTemplate.find(query, Document.class, schemaCollection);
        return documents;
    }

    public List<Document> findAll(String schemaCollection) {
      return mongoTemplate.findAll(Document.class, schemaCollection);
    }
}
