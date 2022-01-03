package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Project;
import com.tek.jpa.domain.Project.ProjectId;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import com.tek.jpa.repository.mock.ProjectRepository;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class ProjectReadOnlyDalService extends ReadOnlyDalService<Project, ProjectId> {

  protected ProjectReadOnlyDalService(
      EntityManager entityManager
  ) {
    super(entityManager);
  }

  @Override
  protected ReadOnlyDalRepository<Project, ProjectId> repository() {
    return new ReadOnlyDalRepository<>(context.getBean(ProjectRepository.class)) {
    };
  }

}
