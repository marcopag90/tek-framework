package com.tek.jpa.controller.impl;

import com.tek.jpa.controller.ReadOnlyDalController;
import com.tek.jpa.service.impl.BaseReadOnlyDalService;
import com.tek.rest.shared.exception.EntityNotFoundException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * <p>Controller that <b>must</b> be extended by a concrete {@link org.springframework.web.bind.annotation.RestController}
 * to expose a read-only <i>CRUD</i> Rest API.
 * <p> A minimal setup requires the following actions:
 * <ul>
 *   <li>
 *     implement the method <i>readAuthorized()</i> to define who is allowed to access the API
 *   </li>
 *   <li>
 *     implement the method <i>getService()</i> to define the {@link com.tek.jpa.service.ReadOnlyDalService} to use.
 *   </li>
 * </ul>
 * <p>E.g:</p>
 * <pre class="code">
 * {@literal @RestController}
 * {@literal @RequestMapping}("books")
 * class BookController extends BaseReadOnlyDalController{@literal <}Book, Long{@literal >} {
 *
 *   {@literal @Override}
 *   public boolean readAuthorized() {
 *     ...
 *   }
 *
 *   {@literal @Override}
 *   public BaseReadOnlyDalService{@literal <}Book, Long{@literal >} getService() {
 *     ...
 *   }
 * }
 * </pre>
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class BaseReadOnlyDalController<E, I> implements ReadOnlyDalController<E, I> {

  @Autowired
  protected ApplicationContext context;

  private BaseReadOnlyDalService<E, I> service;

  @PostConstruct
  void setup() {
    this.service = getService();
  }

  @Override
  public Page<E> findAll(Specification<E> spec, Pageable pageable) {
    return service.findAll(spec, pageable);
  }

  @Override
  public E findById(I id) throws EntityNotFoundException {
    return service.findById(id);
  }
}
