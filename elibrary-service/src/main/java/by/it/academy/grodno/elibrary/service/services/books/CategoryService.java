package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.CategoryJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.mappers.CategoryMapper;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import by.it.academy.grodno.elibrary.entities.books.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Override
    public Class<CategoryDto> getGenericClass() {
        return CategoryDto.class;
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryMapper.toDtos(categoryJpaRepository.findAll());
    }

    @Override
    public Optional<CategoryDto> findById(Integer id) {
        Optional<Category> optionalCategory = categoryJpaRepository.findById(id);
        return optionalCategory.map(category -> categoryMapper.toDto(category));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Optional<Category> optionalCategory = categoryJpaRepository.findById(id);
        optionalCategory.ifPresent(category -> categoryJpaRepository.delete(category));
    }

    @Override
    @Transactional
    public Optional<CategoryDto> create(CategoryDto entityDto) {
        return Optional.of(categoryMapper.toDto(categoryJpaRepository.save(
                    Category.builder()
                            .categoryName(entityDto.getCategoryName())
                            .build())));
    }

    @Override
    @Transactional
    public Optional<CategoryDto> update(Integer id, CategoryDto entityDto) {
        Optional<Category> optionalCategory = categoryJpaRepository.findById(id);
        if (entityDto != null &&
                optionalCategory.isPresent() &&
                entityDto.getCategoryName() != null &&
                !entityDto.getCategoryName().isEmpty()){
            Category category = optionalCategory.get();
            category.setCategoryName(entityDto.getCategoryName());
            category = categoryJpaRepository.save(category);
            return Optional.of(categoryMapper.toDto(category));
        }
        return optionalCategory.map(category -> categoryMapper.toDto(category));
    }

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryMapper.toPageDto(categoryJpaRepository.findAll(pageable));
    }
}
