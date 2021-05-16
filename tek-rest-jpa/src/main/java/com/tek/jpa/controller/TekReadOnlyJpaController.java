package com.tek.jpa.controller;

import com.tek.jpa.repository.TekJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

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
 * <p>A developer can customize the <i>AOP</i> read access in the following way:
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
 * @param <T> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public class TekReadOnlyJpaController<T, I> implements TekReadOnlyJpaApi<T, I> {

  @Autowired
  protected TekJpaRepository<T, I> repository;

  @Override
  public Page<T> findAll(Specification<T> spec, Pageable pageable) {
    return repository.findAll(spec, pageable);
  }

  @Override
  public ResponseEntity<T> findById(I id) {
    return repository.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
