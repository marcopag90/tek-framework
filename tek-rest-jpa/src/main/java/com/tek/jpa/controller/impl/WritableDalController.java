package com.tek.jpa.controller.impl;

import com.tek.jpa.controller.WritableDalApi;
import com.tek.jpa.service.impl.WritableDalService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * <p>Controller that <b>must</b> be extended by a concrete {@link org.springframework.web.bind.annotation.RestController}
 * to expose a <i>CRUD</i> Rest API.
 * <p> A minimal setup requires the following actions:
 * <ul>
 *   <li>
 *     implement the method <i>createAuthorized()</i> to define who is allowed to access the create API
 *   </li>
 *   <li>
 *     implement the method <i>readAuthorized()</i> to define who is allowed to access the read API
 *   </li>
 *   <li>
 *     implement the method <i>updateAuthorized()</i> to define who is allowed to access the update API
 *   </li>
 *   <li>
 *     implement the method <i>deleteAuthorized()</i> to define who is allowed to access the delete API
 *   </li>
 *   <li>
 *     implement the method <i>getWritableDalService()</i> to define the {@link WritableDalService} to use.
 *   </li>
 * </ul>
 * <p>E.g:</p>
 * <pre class="code">
 * {@literal @RestController}
 * {@literal @RequestMapping}("books")
 * class BookController extends WritableDalController{@literal <}Book, Long{@literal >} {
 *
 *   {@literal @Override}
 *   public boolean createAuthorized() {
 *     ...
 *   }
 *
 *   {@literal @Override}
 *   public boolean readAuthorized() {
 *     ...
 *   }
 *
 *   {@literal @Override}
 *   public boolean updateAuthorized() {
 *     ...
 *   }
 *
 *   {@literal @Override}
 *   public boolean deleteAuthorized() {
 *     ...
 *   }
 *
 *   {@literal @Override}
 *   public WritableDalService{@literal <}Book, Long{@literal >} getWritableDalService() {
 *     return context.getBean(BookWritableDalService.class);
 *   }
 * }
 * </pre>
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class WritableDalController<E, I> implements WritableDalApi<E, I> {

  @Autowired
  protected ApplicationContext context;

  protected abstract WritableDalService<E, I> getWritableDalService();

  private WritableDalService<E, I> dalService;

  @PostConstruct
  void setup() {
    this.dalService = getWritableDalService();
  }

  @Override
  public E create(E entity) {
    return dalService.create(entity);
  }

  @Override
  public Page<E> findAll(Specification<E> spec, Pageable page) {
    return dalService.findAll(spec, page);
  }

  @Override
  public E findById(I id) {
    return dalService.findById(id);
  }

  @Override
  public void deleteById(I id) {
    dalService.deleteById(id);
  }
}
