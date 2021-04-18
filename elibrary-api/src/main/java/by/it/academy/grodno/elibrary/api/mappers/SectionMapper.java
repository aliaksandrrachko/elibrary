package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dao.CategoryJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.SectionDto;
import by.it.academy.grodno.elibrary.entities.books.Category;
import by.it.academy.grodno.elibrary.entities.books.Section;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
public class SectionMapper extends AGenericMapper<Section, SectionDto, Integer>{

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    protected SectionMapper(ModelMapper modelMapper) {
        super(modelMapper, Section.class, SectionDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Section.class, SectionDto.class)
                .addMappings(u -> {
                    u.skip(SectionDto::setCategoryId);
                    u.skip(SectionDto::setCategoryName);
                }).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(SectionDto.class, Section.class)
                .addMappings(m -> m.skip(Section::setCategory)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(Section source, SectionDto destination) {
        destination.setCategoryId(source.getCategory().getId());
        destination.setCategoryName(source.getCategory().getCategoryName());
    }

    @Override
    void mapSpecificFields(SectionDto source, Section destination) {
        Integer categoryId = source.getCategoryId();
        String categoryName = source.getCategoryName();
        Optional<Category> optionalCategory = Optional.empty();
        if (categoryId != null){
            optionalCategory = categoryJpaRepository.findById(categoryId);
        } else if (categoryName != null && !categoryName.isEmpty()){
            optionalCategory = categoryJpaRepository.findByCategoryName(categoryName);
        }
        optionalCategory.ifPresent(destination::setCategory);
    }
}
