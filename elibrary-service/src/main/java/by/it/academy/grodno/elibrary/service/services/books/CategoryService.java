package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.CategoryJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.mappers.CategoryMapper;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import by.it.academy.grodno.elibrary.entities.books.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService implements ICategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryService(CategoryMapper categoryMapper, CategoryJpaRepository categoryJpaRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    @Cacheable(value = "categoryList")
    public List<CategoryDto> findAll() {
        log.info("Select all category list with parent null.");
        return categoryMapper.toDtos(categoryJpaRepository.findAllByParentCategoryIsNull());
    }

    @Override
    public Optional<CategoryDto> findById(Integer id) {
        Optional<Category> optionalCategory = categoryJpaRepository.findById(id);
        return optionalCategory.map(categoryMapper::toDto);
    }

    @Override
    @Transactional
    @CacheEvict("categoryList")
    public void delete(Integer id) {
        Optional<Category> optionalCategory = categoryJpaRepository.findById(id);
        optionalCategory.ifPresent(categoryJpaRepository::delete);
    }

    @Override
    @Transactional
    @CacheEvict("categoryList")
    public Optional<CategoryDto> create(CategoryDto entityDto) {
        String parentCategoryName = entityDto.getParentCategory();
        Integer parentCategoryId = entityDto.getParentId();

        Optional<Category> optionalParentCategory = Optional.empty();

        if (StringUtils.hasText(parentCategoryName)) {
            optionalParentCategory = categoryJpaRepository.findByCategoryName(parentCategoryName);
        } else if (entityDto.getParentId() != null && entityDto.getParentId() > 0) {
            optionalParentCategory = categoryJpaRepository.findById(parentCategoryId);
        }
        Category parentCategory = null;
        if (optionalParentCategory.isPresent()) {
            matches(optionalParentCategory.get(), parentCategoryName, parentCategoryId);
            parentCategory = optionalParentCategory.get();
        }


        return Optional.of(categoryMapper.toDto(categoryJpaRepository.save(
                Category.builder()
                        .parentCategory(parentCategory)
                        .categoryName(entityDto.getCategoryName())
                        .build())));
    }

    private void matches(Category category, String parentCategoryName, Integer parentCategoryId) {
        boolean isCategoryName = StringUtils.hasText(parentCategoryName);
        boolean isCategoryId = isCategoryName && parentCategoryId != null && parentCategoryId >= 0;
        if (isCategoryName && isCategoryId &&
                !category.getCategoryName().equals(parentCategoryName)
                && !category.getId().equals(parentCategoryId)) {
            throw new IllegalArgumentException(
                    "Collisions: 'parentCategoryName' and 'parentCategoryId' mismatch for found category.");
        }
    }

    @Override
    @Transactional
    @CacheEvict("categoryList")
    public Optional<CategoryDto> update(Integer id, CategoryDto entityDto) {
        Optional<Category> optionalCategory = categoryJpaRepository.findById(id);
        if (entityDto != null &&
                optionalCategory.isPresent() &&
                entityDto.getCategoryName() != null &&
                !entityDto.getCategoryName().isEmpty()) {
            Category category = optionalCategory.get();
            category.setCategoryName(entityDto.getCategoryName());
            category = categoryJpaRepository.save(category);
            return Optional.of(categoryMapper.toDto(category));
        }
        return optionalCategory.map(categoryMapper::toDto);
    }

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryMapper.toPageDto(categoryJpaRepository.findAll(pageable));
    }

    @Override
    public Optional<CategoryDto> findByCategoryName(String categoryName) {
        Optional<Category> optionalCategory = categoryJpaRepository.findByCategoryName(categoryName);
        return optionalCategory.map(categoryMapper::toDto);
    }

    @Override
    public Set<CategoryDto> findAllUnique() {
        Set<Category> categories = new HashSet<>(categoryJpaRepository.findAll());
        return categories.stream().map(categoryMapper::toDto).collect(Collectors.toSet());
    }
}
