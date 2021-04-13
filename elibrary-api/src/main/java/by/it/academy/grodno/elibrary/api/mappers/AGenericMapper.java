package by.it.academy.grodno.elibrary.api.mappers;


import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import by.it.academy.grodno.elibrary.api.dto.UserDto;
import by.it.academy.grodno.elibrary.entities.AEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AGenericMapper<E extends AEntity<K>,
        D extends AEntityDto<K>,
        K extends Number>
        implements IAGenericMapper<E, D, K> {

    protected final ModelMapper modelMapper;

    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    protected AGenericMapper(ModelMapper modelMapper, Class<E> entityClass, Class<D> dtoClass) {
        this.modelMapper = modelMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public E toEntity(D dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, entityClass);
    }

    @Override
    public D toDto(E entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, dtoClass);
    }

    @Override
    public List<E> toEntities(List<D> sources){
        return Objects.isNull(sources) ?
                Collections.emptyList() :
                modelMapper.map(sources, new TypeToken<List<E>>(){}.getType());
    }

    @Override
    public List<D> toDtos(List<E> sources){
        return Objects.isNull(sources) ?
                Collections.emptyList() :
                modelMapper.map(sources, new TypeToken<List<UserDto>>(){}.getType());
    }

    Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    void mapSpecificFields(E source, D destination){

    }

    void mapSpecificFields(D source, E destination){

    }
}
