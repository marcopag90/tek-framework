package com.tek.mongo.controller;

import static com.tek.rest.shared.constants.TekRestConstants.FILTER_NAME;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.EstimatedDocumentCountOptions;
import com.tek.rest.shared.TekAuthApi;
import com.turkraft.springfilter.boot.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.val;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class TekMongoCollectionController implements TekAuthApi {

  @Autowired
  protected MongoTemplate mongoTemplate;

  @Override
  public final boolean createAuthorized() {
    return false;
  }

  @Override
  public boolean readAuthorized() {
    return isAuthorized();
  }

  @Override
  public final boolean updateAuthorized() {
    return false;
  }

  @Override
  public final boolean deleteAuthorized() {
    return false;
  }

  @GetMapping
  @PreAuthorize(CAN_READ)
  public Page<Document> findAll(
      @RequestParam("collection") String collectionName,
      @Filter(parameterName = FILTER_NAME) Document doc,
      Pageable pageable
  ) {
    List<Document> result = new ArrayList<>();
    MongoCollection<Document> mongoColl = mongoTemplate.getCollection(collectionName);
    FindIterable<Document> documents = doc != null ? mongoColl.find(doc) : mongoColl.find();
    if (pageable.getSort().isSorted()) {
      var withSort = new BasicDBObject();
      for (Sort.Order sort : pageable.getSort()) {
        String property = sort.getProperty();
        int direction = sort.getDirection().isAscending() ? 1 : -1;
        withSort.append(property, direction);
      }
      documents.sort(withSort);
    }
    int withSkip = pageable.getPageNumber() * pageable.getPageSize();
    int withLimit = pageable.getPageSize();
    documents.skip(withSkip).limit(withLimit);
    documents.into(result);
    return new PageImpl<>(result, pageable, count(mongoColl, doc));
  }

  protected long count(MongoCollection<Document> collection, Document filter) {
    if (filter != null) {
      val countOptions = new CountOptions().maxTime(5, TimeUnit.SECONDS);
      return collection.countDocuments(filter, countOptions);
    }
    val countOptions = new EstimatedDocumentCountOptions().maxTime(5, TimeUnit.SECONDS);
    return collection.estimatedDocumentCount(countOptions);
  }
}
