package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.CategoryJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.SectionJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.SectionDto;
import by.it.academy.grodno.elibrary.api.mappers.SectionMapper;
import by.it.academy.grodno.elibrary.api.services.books.ISectionService;
import by.it.academy.grodno.elibrary.entities.books.Category;
import by.it.academy.grodno.elibrary.entities.books.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService implements ISectionService {

    @Autowired
    private SectionMapper sectionMapper;
    @Autowired
    private SectionJpaRepository sectionJpaRepository;
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Override
    public Class<SectionDto> getGenericClass() {
        return SectionDto.class;
    }

    @Override
    public List<SectionDto> findAll() {
        return sectionMapper.toDtos(sectionJpaRepository.findAll());
    }

    @Override
    public Optional<SectionDto> findById(Integer id) {
        Optional<Section> optionalSection = sectionJpaRepository.findById(id);
        return optionalSection.map(section -> sectionMapper.toDto(section));
    }

    @Override
    public void delete(Integer id) {
        Optional<Section> optionalSection = sectionJpaRepository.findById(id);
        optionalSection.ifPresent(section -> sectionJpaRepository.delete(section));
    }

    @Override
    public Optional<SectionDto> create(SectionDto entityDto) {
        Integer categoryId = entityDto.getCategoryId();
        String categoryName = entityDto.getCategoryName();
        Optional<Category> optionalCategory = Optional.empty();
        if (categoryId != null){
            optionalCategory = categoryJpaRepository.findById(categoryId);
        } else if (categoryName != null && !categoryName.isEmpty()){
            optionalCategory = categoryJpaRepository.findByCategoryName(categoryName);
        }

        if (optionalCategory.isPresent()){
            Section section = Section.builder()
                    .sectionName(entityDto.getSectionName())
                    .category(optionalCategory.get())
                    .build();
            return Optional.of(sectionMapper.toDto(sectionJpaRepository.save(section)));
        }
        return Optional.empty();
    }

    @Override
    public Optional<SectionDto> update(Integer id, SectionDto entityDto) {
        Optional<Section> optionalSection = sectionJpaRepository.findById(id);
        if (entityDto != null &&
                optionalSection.isPresent() &&
                entityDto.getSectionName() != null &&
                !entityDto.getSectionName().isEmpty()){
            Section section = optionalSection.get();
            section.setSectionName(entityDto.getSectionName());
            section = sectionJpaRepository.save(section);
            return Optional.of(sectionMapper.toDto(section));
        }
        return optionalSection.map(section -> sectionMapper.toDto(section));
    }

    @Override
    public Page<SectionDto> findAll(Pageable pageable) {
        return sectionMapper.toPageDto(sectionJpaRepository.findAll(pageable));
    }
}
