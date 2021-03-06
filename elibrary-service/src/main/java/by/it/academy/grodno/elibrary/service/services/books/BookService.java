package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.AuthorJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.BookJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.CategoryJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.mappers.BookMapper;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.utils.BookDataProvider;
import by.it.academy.grodno.elibrary.api.utils.IsbnUtils;
import by.it.academy.grodno.elibrary.api.utils.DownloadFileType;
import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.entities.books.Category;
import by.it.academy.grodno.elibrary.entities.books.Publisher;
import by.it.academy.grodno.elibrary.service.utils.FileUploader;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Component
@Slf4j
@Transactional(readOnly = true)
public class BookService implements IBookService {

    private final BookMapper bookMapper;
    private final AuthorJpaRepository authorJpaRepository;
    private final BookJpaRepository bookJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final BookDataProvider bookDataWebProvider;

    public BookService(BookMapper bookMapper, AuthorJpaRepository authorJpaRepository, BookJpaRepository bookJpaRepository,
                       CategoryJpaRepository categoryJpaRepository, BookDataProvider bookDataWebProvider) {
        this.bookMapper = bookMapper;
        this.authorJpaRepository = authorJpaRepository;
        this.bookJpaRepository = bookJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
        this.bookDataWebProvider = bookDataWebProvider;
    }

    @Override
    public List<BookDto> findAll() {
        return bookMapper.toDtos(bookJpaRepository.findAll());
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookJpaRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Book> bookOptional = bookJpaRepository.findById(id);
        bookOptional.ifPresent(bookJpaRepository::delete);
        bookOptional.ifPresent(book -> {
            if (!book.getPictureUrl().equals(DEFAULT_BOOK_COVER_PICTURE_URL_IMAGE)){
                FileUploader.deleteFile(book.getPictureUrl());
            }
        });
    }

    @Override
    @Transactional
    public BookDto create(BookDto entityDto) {
        Book book = prepareBookToSave(entityDto);
        book = bookJpaRepository.save(book);
        return bookMapper.toDto(book);
    }

    private Book prepareBookToSave(BookDto entityDto) {
        entityDto.setCreated(LocalDateTime.now().withNano(0));
        entityDto.setUpdated(LocalDateTime.now().withNano(0));
        Book newBookData = bookMapper.toEntity(entityDto);
        createAndSetPublisherIfNotExists(newBookData, entityDto);
        createAndSetAuthorIfNotExists(newBookData, entityDto);
        setDefaultBookCoverImageIfNot(newBookData);
        return newBookData;
    }

    @Override
    @Transactional
    public Optional<BookDto> create(BookDto entityDto, MultipartFile file) {
        Book book = prepareBookToSave(entityDto);
        if (file != null && !file.isEmpty()) {
            try {
                URL bookCoverUri = FileUploader.uploadFile(file, DownloadFileType.BOOK_COVER, String.valueOf(book.getId()));
                book.setPictureUrl(bookCoverUri.getPath());
            } catch (IOException e) {
                log.error("Error uploading book cover image : '{}'.", file.getName());
            }
        }
        book = bookJpaRepository.save(book);
        return Optional.of(bookMapper.toDto(book));
    }

    private void createAndSetPublisherIfNotExists(Book book, BookDto bookDto) {
        if (book.getPublisher() == null) {
            Publisher publisher = new Publisher(bookDto.getPublisher());
            book.setPublisher(publisher);
        }
    }

    private void createAndSetAuthorIfNotExists(Book book, BookDto bookDto) {
        Set<String> booksAuthors = book.getAuthors().stream().map(Author::getAuthorName)
                .collect(Collectors.toSet());
        bookDto.getAuthors().forEach(authorName -> {
            if (!booksAuthors.contains(authorName) && StringUtils.hasText(authorName)) {
                book.getAuthors().add(new Author(authorName));
            }
        });
    }

    @Override
    @Transactional
    public BookDto update(Long id, BookDto entityDto) {
        Book book = prepareBookToUpdating(id, entityDto);
        if (book != null) {
            book = bookJpaRepository.save(book);
            return bookMapper.toDto(book);
        }
        return null;
    }

    @Override
    @Transactional
    public Optional<BookDto> update(Long id, BookDto entityDto, MultipartFile file) {
        Book book = prepareBookToUpdating(id, entityDto);
        if (book == null){
            return Optional.empty();
        }
        if (file != null && !file.isEmpty()){
            try {
                URL pictureUrl = FileUploader.uploadFile(file, DownloadFileType.BOOK_COVER, String.valueOf(book.getId()));
                book.setPictureUrl(pictureUrl.toString());
            } catch (IOException e) {
                log.error("Error uploading books cover image : '{}'.", file.getName());
            }
        }
        book = bookJpaRepository.save(book);
        return Optional.of(bookMapper.toDto(book));
    }

    @Override
    public List<BookDto> findTop6ByRating() {
        return bookMapper.toDtos(bookJpaRepository.findAll(
                PageRequest.of(0, 6, Sort.Direction.DESC, "rating")).getContent());
    }

    @Override
    public Page<BookDto> findAllBooks(Integer categoryId, String title, String author, Pageable pageable) {
        Page<BookDto> pageBookDto;
        if (categoryId != null) {
            pageBookDto = findAllIncludeSubCategories(categoryId, pageable);
        } else if (title != null) {
            pageBookDto = findAllByTitle(title, pageable);
        } else if (author != null) {
            pageBookDto = findAllByAuthorName(author, pageable);
        } else {
            pageBookDto = findAll(pageable);
        }
        return pageBookDto;
    }

    private Book prepareBookToUpdating(Long id, BookDto entityDto){
        Optional<Book> optionalBook = bookJpaRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book bookFromDb = optionalBook.get();
            entityDto.setCreated(bookFromDb.getCreated());
            entityDto.setUpdated(LocalDateTime.now().withNano(0));
            Book newBookData = bookMapper.toEntity(entityDto);
            newBookData.setId(id);
            createAndSetPublisherIfNotExists(newBookData, entityDto);
            createAndSetAuthorIfNotExists(newBookData, entityDto);
            setDefaultBookCoverImageIfNot(newBookData);
            return newBookData;
        }
        return null;
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookMapper.toPageDto(bookJpaRepository.findAll(pageable));
    }

    @Override
    public Page<BookDto> findAllByCategoryId(Integer categoryId, Pageable pageable) {
        return bookMapper.toPageDto(bookJpaRepository.findAllByCategoryId(categoryId, pageable));
    }

    @Override
    public Page<BookDto> findAll(Integer categoryId, Pageable pageable) {
        if (categoryId == null) {
            return findAll(pageable);
        } else {
            return findAllByCategoryId(categoryId, pageable);
        }
    }

    @Override
    public Page<BookDto> findAllByTitle(String title, Pageable pageable) {
        return bookMapper.toPageDto(bookJpaRepository.findAllByTitleContaining(title, pageable));
    }

    @Override
    public Page<BookDto> findAllByAuthorName(String author, Pageable pageable) {
        List<Author> authors = authorJpaRepository.findByAuthorNameContaining(author);
        return bookMapper.toPageDto(bookJpaRepository.findAllByAuthorsIn(authors, pageable));
    }

    @Override
    @Transactional
    public void setAvailability(long bookId) {
        Optional<Book> bookOptional = bookJpaRepository.findById(bookId);
        bookOptional.ifPresent(book -> {
            book.setAvailable(!book.isAvailable());
            bookJpaRepository.save(book);
        });
    }

    @Override
    public Optional<BookDto> findByIsbnInWeb(@NotNull String isbn) {
        String isbn13 = IsbnUtils.toIsbn13(isbn);
        return bookDataWebProvider.getBook(isbn13).map(bookMapper::toDto);
    }

    @Override
    public Page<BookDto> findAllIncludeSubCategories(Integer categoryId, Pageable pageable) {
        Optional<Category> parentCategoryOptional = categoryJpaRepository.findById(categoryId);
        if (!parentCategoryOptional.isPresent()) {
            return Page.empty(pageable);
        }
        Set<Category> allCategoryIncludeSubCategories = getNestedCategory(parentCategoryOptional.get());
        return bookMapper.toPageDto(bookJpaRepository.findByCategoryIn(allCategoryIncludeSubCategories, pageable));
    }

    private Set<Category> getNestedCategory(Category category) {
        Queue<Category> categoryQueue = new LinkedBlockingQueue<>();
        categoryQueue.add(category);
        Set<Category> categorySet = new HashSet<>();
        while (!categoryQueue.isEmpty()) {
            Category categoryFromQueue = categoryQueue.poll();
            Set<Category> subCategories = categoryFromQueue.getCategories();
            if (subCategories != null && !subCategories.isEmpty()) {
                categoryQueue.addAll(subCategories);
            }
            categorySet.add(categoryFromQueue);
        }
        return categorySet;
    }

    private static final String DEFAULT_BOOK_COVER_PICTURE_URL_IMAGE = "/img/books/covers/default_book_cover.png";

    private void setDefaultBookCoverImageIfNot(Book book){
        if (!StringUtils.hasText(book.getPictureUrl())){
            book.setPictureUrl(DEFAULT_BOOK_COVER_PICTURE_URL_IMAGE);
        }
    }
}