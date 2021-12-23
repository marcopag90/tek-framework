package com.tek.jpa.controller.impl;

import com.tek.jpa.controller.WritableDalApi;
import com.tek.jpa.service.WritableDalService;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 * <p>Controller that <b>must</b> be extended by a concrete {@link org.springframework.web.bind.annotation.RestController}
 * to expose a <i>CRUD</i> Rest API.
 * <p> A minimal setup requires the following actions:
 * <ul>
 *   <li>
 *     implement the method <i>createAuthorized()</i> to define who is allowed to access the create API
 *   </li>
 *   <li>
 *     implement the method <i>readAuthorized()</i> to define who is allowed to access the read API
 *   </li>
 *   <li>
 *     implement the method <i>updateAuthorized()</i> to define who is allowed to access the update API
 *   </li>
 *   <li>
 *     implement the method <i>deleteAuthorized()</i> to define who is allowed to access the delete API
 *   </li>
 *   <li>
 *     implement the method <i>getWritableDalService()</i> to define the {@link WritableDalService} to use.
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
 *   public WritableDalService{@literal <}Book, Long{@literal >} getWritableDalService() {
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

  protected abstract WritableDalService<E, I> dalService();

  private WritableDalService<E, I> dalService;

  @Override
  @PostConstruct
  void setup() {
    this.dalService = dalService();
  }

  @Override
  public E create(E entity) {
    return dalService.create(entity);
  }

  @Override
  public void deleteById(I id) {
    dalService.deleteById(id);
  }
}
