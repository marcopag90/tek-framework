package com.tek.core.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.metamodel.EntityType;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Utility service wrapping {@link javax.persistence.EntityManager}
 * <p>
 * Allows to rsql the Jpa Manager to find:
 * <ul>
 *     <li>Class annotated with {@link javax.persistence.Entity}</li>
 *     <li>Type of the field annotated with {@link Id} </li>
 *     <li>{@link org.springframework.stereotype.Repository} of the entity</li>
 * </ul>
 * <p>
 *
 * @author MarcoPagan
 * @throws IllegalArgumentException if the target method fails the inquiry.
 * @throws NotImplementedException if the method lacks implementations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
//TODO Manager test
public class TekEntityManager {

    @NonNull private final EntityManager entityManager;
    @NonNull private final ApplicationContext applicationContext;

    @Getter public Set<EntityType<?>> entityTypes;
    @Getter public Repositories repositories;

    @PostConstruct
    private void init() {
        entityTypes = entityManager.getMetamodel().getEntities();
        repositories = new Repositories(applicationContext);
    }

    public Class<?> getClass(String entityName) {
        return getEntityType(entityName).getClass();
    }

    public Object getIdType(Class<?> clazz, String stringId) {
        log.debug("Trying to retrieve id type for class: [{}]", clazz.getSimpleName());

        Field field = Arrays.stream(clazz.getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(Id.class)).findFirst().orElse(null);
        String ann = Id.class.getSimpleName();

        Assert.notNull(field, String.format("Unable to find %s annotation", ann));

        Class<?> declaringClass = field.getDeclaringClass();
        if (declaringClass.equals(String.class)) {
            return stringId;
        } else if (declaringClass.equals(Integer.class)) {
            return Integer.valueOf(stringId);
        } else if (declaringClass.equals(Long.class)) {
            return Long.valueOf(stringId);
        } else if (declaringClass.equals(BigDecimal.class)) {
            return BigDecimal.valueOf(Integer.parseInt(stringId));
        } else if (declaringClass.equals(UUID.class)) {
            return UUID.fromString(stringId);
        } else {
            throw new NotImplementedException("Operation not yet implented.");
        }
    }

    public JpaRepository<?, ?> getRepository(String entityName) {
        log.debug("Trying to retrieve repository for: [{}]", entityName);

        EntityType<?> entityType = getEntityType(entityName);
        Optional<Object> repositoryFor = repositories.getRepositoryFor(entityType.getClass());

        Assert.isTrue(
            repositoryFor.isPresent(),
            String.format("Unable to find a suitable repository for %s", entityName)
        );

        return (JpaRepository<?, ?>) repositoryFor.get();
    }

    private EntityType<?> getEntityType(String name) {
        log.debug("Trying to retrieve entity by name: [{}]", name);

        EntityType<?> entityType = entityTypes.stream()
            .filter(e -> e.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        Assert.notNull(entityType, String.format("Unable to find a suitable class for %s.", name));

        return entityType;
    }
}
