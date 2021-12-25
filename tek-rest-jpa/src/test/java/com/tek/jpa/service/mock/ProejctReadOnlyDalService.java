package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Project;
import com.tek.jpa.domain.Project.ProjectId;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import com.tek.jpa.repository.mock.ProjectRepository;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ProejctReadOnlyDalService extends ReadOnlyDalService<Project, ProjectId> {

  @Override
  protected EntityManager entityManager() {
    return context.getBean(EntityManager.class);
  }

  @Bean
  @Override
  protected ReadOnlyDalRepository<Project, ProjectId> dalRepository() {
    return new ReadOnlyDalRepository<>(context.getBean(ProjectRepository.class)) {
    };
  }
}
