package com.tek.jpa.controller;

import com.tek.jpa.service.ReadOnlyDalService;
import javax.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * <p>Controller that <b>must</b> be extended by a concrete {@link org.springframework.web.bind.annotation.RestController}
 * to expose a read-only <i>CRUD</i> JPA Rest API.
 * <p>The only annotation required in the extended class is the @{@link
 * org.springframework.web.bind.annotation.RequestMapping} annotation, eg:
 * <pre class="code">
 * {@literal @RestController}
 * {@literal @RequestMapping}("books")
 * class BookController extends TekReadOnlyJpaController{@literal <}Book, Long{@literal >} {}
 * </pre>
 *
 * <p>A developer has to implement the <i>AOP</i> read access in the following way:
 * <pre class="code">
 * {@literal @RestController}
 * {@literal @RequestMapping}("books")
 * class BookController extends TekReadOnlyJpaController{@literal <}Book, Long{@literal >} {
 *
 *   {@literal @Override}
 *   public boolean readAuthorized() {
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

  protected abstract ReadOnlyDalService<E, I> getReadOnlyDalService();

  protected ReadOnlyDalService<E, I> readOnlyDalService;

  @PostConstruct
  void setup() {
    this.readOnlyDalService = getReadOnlyDalService();
  }

  @Override
  public Page<E> findAll(Specification<E> spec, Pageable pageable) {
    return readOnlyDalService.findAll(spec, pageable);
  }

  @Override
  public E findById(I id) {
    return readOnlyDalService.findById(id);
  }
}
