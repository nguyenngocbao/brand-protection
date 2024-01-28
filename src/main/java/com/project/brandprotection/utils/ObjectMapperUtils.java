package com.project.brandprotection.utils;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ObjectMapperUtils {

    private final ModelMapper mapper;

    /**
     * Converts an entity to a DTO or vice versa.
     *
     * @param entity      The source entity.
     * @param outputClass The target class (DTO or entity).
     * @param <C>         The type of the target class.
     * @param <E>         The type of the source entity.
     * @return The converted object of type C.
     */
    public <C,E> C mapToEntityOrDto(final E entity, Class<C> outputClass){
        return mapper.map(entity, outputClass);
    }

    /**
     * Converts data from a DTO to an already persisted entity, updating the entity with the DTO's data.
     *
     * @param entity The already persisted entity to update.
     * @param dto    The DTO containing the data to update the entity.
     * @param <E>    The type of the target entity.
     * @param <D>    The type of the source DTO.
     */
    public <E, D> void mapToPersistedEntity(final E entity, final D dto){
        mapper.map(dto, entity);
    }

    /**
     * Maps a collection of entities to a collection of DTOs.
     *
     * @param input       The collection of source entities.
     * @param outputClass The target class (DTO).
     * @param <C>         The type of the target class (DTO).
     * @param <E>         The type of the source entities.
     * @return A list of converted DTOs.
     */
    public <C,E> List<C> mapAll(final Collection<? extends E> input, final Class<C> outputClass){
        return input.stream().map(
                entity -> mapToEntityOrDto(entity, outputClass)
        ).collect(Collectors.toList());
    }

}