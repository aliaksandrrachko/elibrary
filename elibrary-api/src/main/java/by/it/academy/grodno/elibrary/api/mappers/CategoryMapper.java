package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.entities.books.Category;
import by.it.academy.grodno.elibrary.entities.books.Section;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryMapper extends AGenericMapper<Category, CategoryDto, Integer>{

    protected CategoryMapper(ModelMapper modelMapper) {
        super(modelMapper, Category.class, CategoryDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Category.class, CategoryDto.class)
                .addMappings(u -> u.skip(CategoryDto::setSections)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(CategoryDto.class, Category.class)
                .addMappings(m -> m.skip(Category::setSections)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Category source, CategoryDto destination) {
        Set<String> sections = source.getSections().stream()
                .map(Section::getSectionName).collect(Collectors.toSet());
        destination.setSections(sections);
    }

    @Override
    public void mapSpecificFields(CategoryDto source, Category destination) {
        //destination.setGender(Gender.getGender(source.getGender()));
    }
}
