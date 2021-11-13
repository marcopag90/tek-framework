package com.tek.jpa.controller;

import com.tek.jpa.repository.TekJpaRepository;
import com.tek.jpa.service.TekJpaReadOnlyService;
import org.springframework.beans.factory.annotation.Autowired;
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
public abstract class TekReadOnlyJpaController<E, I> implements TekReadOnlyJpaApi<E, I> {

  @Autowired protected TekJpaRepository<E, I> repository;
  @Autowired protected TekJpaReadOnlyService<E, I> readOnlyService;

  @Override
  public Page<E> findAll(Specification<E> spec, Pageable pageable) {
    return readOnlyService.findAll(spec, pageable);
  }

  @Override
  public E findById(I id) {
    return readOnlyService.findById(id);
  }
}
