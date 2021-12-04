package com.tek.jpa.controller.impl;

import com.tek.jpa.controller.WritableCrudApi;

/**
 * <p>Controller that <b>must</b> be extended by a concrete {@link org.springframework.web.bind.annotation.RestController}
 * to expose a <i>CRUD</i> JPA Rest API.
 * <p>The only annotation required in the extended class is the @{@link
 * org.springframework.web.bind.annotation.RequestMapping} annotation, eg:
 * <pre class="code">
 * {@literal @RestController}
 * {@literal @RequestMapping}("books")
 * class BookController extends TekWritableJpaController{@literal <}Book, Long{@literal >} {}
 * </pre>
 *
 * <p>A developer has to implement the <i>AOP crud</i> access in the following way:
 * <pre class="code">
 * {@literal @RestController}
 * {@literal @RequestMapping}("books")
 * class BookController extends TekWritableJpaController{@literal <}Book, Long{@literal >} {
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
 * }
 * </pre>
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class WritableCrudController<E, I>
    extends ReadOnlyCrudController<E, I> implements WritableCrudApi<E, I> {

//  @Override
//  public E create(E entity) {
//    return repository.save(entity);
//  }
//
//  @Override
//  public I delete(I id) {
//    repository.deleteById(id);
//    return id;
//  }
}
