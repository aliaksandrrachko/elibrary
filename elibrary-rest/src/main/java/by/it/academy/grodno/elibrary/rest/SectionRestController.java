package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.SectionDto;
import by.it.academy.grodno.elibrary.api.services.books.ISectionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "rest/sections")
public class SectionRestController {

    private final ISectionService sectionService;

    public SectionRestController(ISectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping()
    public List<SectionDto> findAllSection() {
        return sectionService.findAll();
    }

    @GetMapping(value = "/{id}")
    public SectionDto findSection(@PathVariable Integer id) {
        return sectionService.findById(id).orElse(null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public SectionDto createCategory(@Valid @RequestBody SectionDto dto) {
        return sectionService.create(dto).orElse(null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SectionDto updateCategory(@Valid @RequestBody SectionDto dto, @PathVariable Integer id) {
        return sectionService.update(id, dto).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteSection(@PathVariable Integer id) {
        sectionService.delete(id);
    }
}
