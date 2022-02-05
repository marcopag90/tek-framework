package com.tek.jpa.controller;

import com.tek.jpa.service.IWritableDalService;
import com.tek.jpa.service.WritableDalService;
import com.tek.rest.shared.dto.UpdateRequest;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.Serializable;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * <p>Controller that <b>must</b> be extended by a concrete {@link org.springframework.web.bind.annotation.RestController}
 * to expose a <i>CRUD</i> Rest API.
 * <p> A minimal setup requires the following actions:
 * <ul>
 *   <li>
 *     implement the <i>createAuthorized()</i> method to define who is allowed to access the create API;
 *   </li>
 *   <li>
 *     implement the <i>readAuthorized()</i> method to define who is allowed to access the read API;
 *   </li>
 *   <li>
 *     implement the <i>updateAuthorized()</i> method to define who is allowed to access the update API;
 *   </li>
 *   <li>
 *     implement the <i>deleteAuthorized()</i> method to define who is allowed to access the delete API;
 *   </li>
 *   <li>
 *     implement the <i>service()</i> method to define the {@link WritableDalService} to use.
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
 *   public IWritableDalService{@literal <}Book, Long{@literal >} service() {
 *     return context.getBean(BookWritableDalService.class);
 *   }
 * }
 * </pre>
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class WritableDalController<E extends Serializable, I extends Serializable>
    extends ReadOnlyDalController<E, I> implements WritableDalApi<E, I> {

  protected abstract IWritableDalService<E, I> service();

  @Override
  public E create(E entity) throws MethodArgumentNotValidException {
    return service().create(entity);
  }

  @Override
  public E update(I id, UpdateRequest request)
      throws EntityNotFoundException, NoSuchFieldException, MethodArgumentNotValidException {
    return service().update(id, request.getProperties(), request.getVersion());
  }

  @Override
  public void deleteById(I id) throws EntityNotFoundException {
    service().deleteById(id);
  }
}
