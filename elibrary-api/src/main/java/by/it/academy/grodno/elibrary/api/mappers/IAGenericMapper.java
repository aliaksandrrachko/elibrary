package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import by.it.academy.grodno.elibrary.entities.AEntity;

import java.util.List;

public interface IAGenericMapper<E extends AEntity<K>,
        D extends AEntityDto<K>,
        K extends Number> {

    E toEntity(D dto);
    D toDto(E entity);
    List<E> toEntities(List<D> sources);
    List<D> toDtos(List<E> sources);
}
