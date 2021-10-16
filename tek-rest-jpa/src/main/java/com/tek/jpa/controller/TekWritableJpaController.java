package com.tek.jpa.controller;

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
 * <p>A developer can customize the <i>AOP crud</i> access in the following way:
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
 * <p>A globally configured <i>AOP crud</i> access can be achieved in the following way:
 * <pre class="code">
 * {@literal @RestController}
 * {@literal @RequestMapping}("books")
 * class BookController extends TekWritableJpaController{@literal <}Book, Long{@literal >} {
 *
 *   {@literal @Override}
 *   public boolean isAuthorized() {
 *     ...
 *   }
 * }
 * </pre>
 *
 * @param <T> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public class TekWritableJpaController<T, I>
    extends TekReadOnlyJpaController<T, I> implements TekWritableJpaApi<T, I> {

  @Override
  public T create(T entity) {
    return repository.save(entity);
  }

  @Override
  public I delete(I id) {
    repository.deleteById(id);
    return id;
  }
}
