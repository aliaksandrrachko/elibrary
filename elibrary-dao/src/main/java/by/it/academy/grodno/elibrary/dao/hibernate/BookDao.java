package by.it.academy.grodno.elibrary.dao.hibernate;

import by.it.academy.grodno.elibrary.api.dao.hibernate.IBookDao;
import by.it.academy.grodno.elibrary.entities.books.Book;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public Page<Book> findAllBetween(LocalDateTime from, LocalDateTime to, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(getGenericClass());

        Root<Book> root = cq.from(getGenericClass());
        CriteriaQuery<Book> select = cq.select(root);
        addBetweenCreateDateCondition(from, to, cb, root, select);

        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<Book> countRoot = countCq.from(getGenericClass());
        CriteriaQuery<Long> countSelect = countCq.select(cb.count(countRoot));
        addBetweenCreateDateCondition(from,to, cb, countRoot, countSelect);
        TypedQuery<Long> countTypedQuery = entityManager.createQuery(countSelect);
        Long countResult = countTypedQuery.getSingleResult();


        List<Order> orders = pageable.getSort().stream().map(order -> {
            String property = order.getProperty();
            boolean ascending = order.getDirection().isAscending();
            return new OrderImpl(root.get(property), ascending);
        }).collect(Collectors.toList());
        select.orderBy(orders);

        TypedQuery<Book> typedQuery = entityManager.createQuery(select)
                .setFirstResult(calculateOffset(pageable.getPageNumber(), pageable.getPageSize()))
                .setMaxResults(pageable.getPageSize());
        List<Book> resultList = typedQuery.getResultList();

        return new PageImpl<>(resultList, pageable, countResult);
    }

    private void addBetweenCreateDateCondition(LocalDateTime from, LocalDateTime to,
                                               CriteriaBuilder cb, Root<Book> root, CriteriaQuery<?> select) {
        if (from != null && to != null) {
            select.where(cb.between(root.get("createDate"), from, to));
        } else if (from != null) {
            select.where(cb.greaterThan(root.get("createDate"), from));
        } else if (to != null) {
            select.where(cb.lessThan(root.get("createDate"), to));
        }
    }

    private int calculateOffset(int page, int limit) {
        return ((limit * (page + 1)) - limit);
    }
}
