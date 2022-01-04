package com.tek.jpa.service;

import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.Serializable;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface IWritableDalService<E extends Serializable, I extends Serializable>
    extends IReadOnlyDalService<E, I> {

  E create(@NonNull E entity) throws MethodArgumentNotValidException;

  E update(
      @NonNull I id,
      @NonNull Map<String, Serializable> properties,
      @Nullable Serializable version
  ) throws
      AccessDeniedException,
      MethodArgumentNotValidException,
      EntityNotFoundException,
      NoSuchFieldException;

  void deleteById(@NonNull I id) throws EntityNotFoundException;

}
