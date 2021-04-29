package com.tek.mongo.repository;

import com.turkraft.springfilter.repository.DocumentExecutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

public class TekMongoRepositoryImpl<T, I>
    extends SimpleMongoRepository<T, I> implements DocumentExecutor<T, I> {

  private final MongoEntityInformation<T, I> metadata;
  private final MongoOperations mongoOperations;

  public TekMongoRepositoryImpl(MongoEntityInformation<T, I> metadata, MongoOperations op) {
    super(metadata, op);
    this.metadata = metadata;
    this.mongoOperations = op;
  }

  @Override
  public MongoEntityInformation<T, I> getMetadata() {
    return this.metadata;
  }

  @Override
  public MongoOperations getMongoOperations() {
    return this.mongoOperations;
  }
}
