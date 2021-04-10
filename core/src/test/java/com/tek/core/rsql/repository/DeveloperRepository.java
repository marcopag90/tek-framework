package com.tek.core.rsql.repository;

import com.tek.core.rsql.model.Developer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DeveloperRepository
    extends CrudRepository<Developer, Integer>, JpaSpecificationExecutor<Developer> {
}
