package db.sharding.rw01.service;

import db.sharding.rw01.dao.BookRepository;
import db.sharding.rw01.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Iterable<Book> getAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }
}
