package by.it.academy.grodno.elibrary.dao.hibernate;

import by.it.academy.grodno.elibrary.api.dao.hibernate.IBookDao;
import by.it.academy.grodno.elibrary.entities.books.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.TypedQuery;
import java.util.List;

public class BookDao extends AGenericDao<Book, Long> implements IBookDao {

    public BookDao() {
        super(Book.class);
    }

    @Override
    public Page<Book> findAllByCategoryId(Integer categoryId, Pageable pageable) {
        String countQ = "SELECT count(b.id) FROM Book b WHERE b.category = :categoryId";
        TypedQuery<Long> countQuery = entityManager.createQuery(countQ, Long.class);
        countQuery.setParameter("categoryId", categoryId);
        Long total = countQuery.getSingleResult();
        List<Book> books = entityManager.createQuery("SELECT b FROM Book b WHERE b.category = :categoryId", Book.class)
                .setParameter("categoryId", categoryId)
                .setFirstResult(calculateOffset(pageable.getPageNumber(), pageable.getPageSize()))
                .setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(books, pageable, total);
    }

    @Override
    public Page<Book> findAllByTitleLike(String title, Pageable pageable) {
        String countQ = "SELECT count(b.id) FROM Book b WHERE b.title LIKE :title";
        Long total = entityManager.createQuery(countQ, Long.class)
                .setParameter("title", title).getSingleResult();

        String bookLikeTitleQuery = "SELECT b FROM Book b WHERE b.title LIKE :title";
        List<Book> bookList = entityManager.createQuery(bookLikeTitleQuery, Book.class)
                .setParameter("title", title)
                .setFirstResult(calculateOffset(pageable.getPageNumber(), pageable.getPageSize()))
                .setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(bookList, pageable, total);
    }

    private int calculateOffset(int page, int limit) {
        return ((limit * (page + 1)) - limit);
    }
}
