package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Project;
import com.tek.jpa.domain.Project.ProjectId;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ProjectReadOnlyDalService extends ReadOnlyDalService<Project, ProjectId> {

  protected ProjectReadOnlyDalService(
      ApplicationContext context,
      EntityManager entityManager
  ) {
    super(context, entityManager);
  }
}
