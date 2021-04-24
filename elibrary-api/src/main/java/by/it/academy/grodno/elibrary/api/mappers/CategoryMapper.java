package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dao.CategoryJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.entities.books.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryMapper extends AGenericMapper<Category, CategoryDto, Integer>{

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    protected CategoryMapper(ModelMapper modelMapper) {
        super(modelMapper, Category.class, CategoryDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Category.class, CategoryDto.class)
                .addMappings(u -> {
                    u.skip(CategoryDto::setCategories);
                    u.skip(CategoryDto::setParentId);
                    u.skip(CategoryDto::setParentCategory);
                    u.skip(CategoryDto::setCategoryPath);
                }).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(CategoryDto.class, Category.class)
                .addMappings(m -> {
                    m.skip(Category::setCategories);
                    m.skip(Category::setParentCategory);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Category source, CategoryDto destination) {
        if (source.getParentCategory() != null) {
            destination.setParentCategory(source.getParentCategory().getCategoryName());
            destination.setParentId(source.getParentCategory().getId());
        }
        destination.setCategoryPath(source.getPath());
        Set<CategoryDto> categories = source.getCategories().stream().map(this::toDto).collect(Collectors.toSet());
        destination.setCategories(categories);
    }

    @Override
    public void mapSpecificFields(CategoryDto source, Category destination) {
        destination.setParentCategory(categoryJpaRepository.findByCategoryName(source.getParentCategory()).orElse(null));
        Set<Category> categories = source.getCategories().stream().map(this::toEntity).collect(Collectors.toSet());
        destination.setCategories(categories);
    }
}
