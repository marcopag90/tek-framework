package com.tek.mongo.repository;

import com.turkraft.springfilter.repository.DocumentExecutor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TekMongoRepository<T, I> extends MongoRepository<T, I>, DocumentExecutor<T, I> {

}
