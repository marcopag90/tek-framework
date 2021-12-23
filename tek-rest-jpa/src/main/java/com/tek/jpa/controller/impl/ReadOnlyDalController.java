package com.tek.jpa.controller.impl;

import com.tek.jpa.controller.ReadOnlyDalApi;
import com.tek.jpa.service.ReadOnlyDalService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ClassUtils;

/**
 * <p>Controller that <b>must</b> be extended by a concrete {@link org.springframework.web.bind.annotation.RestController}
 * to expose a read-only <i>CRUD</i> Rest API.
 * <p> A minimal setup requires the following actions:
 * <ul>
 *   <li>
 *     implement the method <i>readAuthorized()</i> to define who is allowed to access the API
 *   </li>
 *   <li>
 *     implement the method <i>getReadOnlyDalService()</i> to define the {@link ReadOnlyDalService} to use.
 *   </li>
 * </ul>
 * <p>E.g:</p>
 * <pre class="code">
 * {@literal @RestController}
 * {@literal @RequestMapping}("books")
 * class BookController extends ReadOnlyDalController{@literal <}Book, Long{@literal >} {
 *
 *   {@literal @Override}
 *   public boolean readAuthorized() {
 *     ...
 *   }
 *
 *   {@literal @Override}
 *   public ReadOnlyDalService{@literal <}Book, Long{@literal >} getReadOnlyDalService() {
 *     return context.getBean(BookReadOnlyDalService.class);
 *   }
 * }
 * </pre>
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class ReadOnlyDalController<E extends Serializable, I extends Serializable>
    implements ReadOnlyDalApi<E, I> {

  protected Logger log = LoggerFactory.getLogger(ClassUtils.getUserClass(this).getSimpleName());

  @Autowired
  protected ApplicationContext context;

  protected abstract ReadOnlyDalService<E, I> dalService();

  private ReadOnlyDalService<E, I> dalService;

  @PostConstruct
  void setup() {
    this.dalService = dalService();
  }

  @Override
  public Page<E> findAll(Specification<E> spec, Pageable pageable) {
    return dalService.findAll(spec, pageable);
  }

  @Override
  public E findById(I id) {
    return dalService.findById(id);
  }
}
