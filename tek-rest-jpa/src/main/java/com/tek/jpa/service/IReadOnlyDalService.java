package com.tek.jpa.service;

import com.tek.rest.shared.dto.ApiPage;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.Serializable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface IReadOnlyDalService<E extends Serializable, I extends Serializable> {

  ApiPage<E> findAll(@Nullable Specification<E> specification, @NonNull Pageable pageable);

  E findById(@NonNull I id) throws EntityNotFoundException;
}
