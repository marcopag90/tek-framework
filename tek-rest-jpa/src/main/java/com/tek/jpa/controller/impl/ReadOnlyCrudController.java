package com.tek.jpa.controller.impl;

import com.tek.jpa.controller.ReadOnlyCrudApi;
import com.tek.jpa.service.impl.BaseReadOnlyDalService;
import com.tek.rest.shared.exception.EntityNotFoundException;
import javax.annotation.PostConstruct;
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
 *     implement the method <i>getDalService()</i> to define the {@link com.tek.jpa.service.ReadOnlyDalService} to use.
 *   </li>
 * </ul>
 * <p>E.g:</p>
 * <pre class="code">
 * {@literal @RestController}
 * {@literal @RequestMapping}("books")
 * class BookController extends ReadOnlyCrudController{@literal <}Book, Long{@literal >} {
 *
 *   {@literal @Override}
 *   public boolean readAuthorized() {
 *     ...
 *   }
 *
 *   {@literal @Override}
 *   protected BaseReadOnlyDalService{@literal <}Book, Long{@literal >} getDalService() {
 *     ...
 *   }
 * }
 * </pre>
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class ReadOnlyCrudController<E, I> implements ReadOnlyCrudApi<E, I> {

  protected abstract BaseReadOnlyDalService<E, I> getDalService();

  private BaseReadOnlyDalService<E, I> readOnlyDalService;

  @PostConstruct
  void setup() {
    this.readOnlyDalService = getDalService();
  }

  @Override
  public Page<E> findAll(Specification<E> spec, Pageable pageable) {
    return readOnlyDalService.findAll(spec, pageable);
  }

  @Override
  public E findById(I id) throws EntityNotFoundException {
    return readOnlyDalService.findById(id);
  }
}
