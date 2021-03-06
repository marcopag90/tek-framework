package com.tek.jpa.controller;

import com.tek.jpa.service.IReadOnlyDalService;
import com.tek.jpa.service.ReadOnlyDalService;
import com.tek.rest.shared.dto.ApiPage;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ClassUtils;

/**
 * <p>Controller that <b>must</b> be extended by a concrete {@link org.springframework.web.bind.annotation.RestController}
 * to expose a read-only Rest API.
 * <p> A minimal setup requires the following actions:
 * <ul>
 *   <li>
 *     implement the <i>readAuthorized()</i> method to define who is allowed to access the API;
 *   </li>
 *   <li>
 *     implement the <i>service()</i> method to define the {@link ReadOnlyDalService} to use.
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
 *   public IReadOnlyDalService{@literal <}Book, Long{@literal >} service() {
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

  protected abstract IReadOnlyDalService<E, I> service();

  @Override
  public ApiPage<E> findAll(Specification<E> spec, Pageable pageable) {
    return service().findAll(spec, pageable);
  }

  @Override
  public E findById(I id) throws EntityNotFoundException {
    return service().findById(id);
  }
}
