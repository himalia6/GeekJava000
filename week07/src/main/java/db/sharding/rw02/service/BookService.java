package db.sharding.rw02.service;

import db.sharding.common.domain.Book;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BookService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public Book save(Book book) {
        final Book result = jdbcTemplate.execute("INSERT INTO book (isbn, title, author, price, published_at) VALUES (?, ?, ?, ?, ?)",
                (PreparedStatementCallback<Book>) ps -> {
                    ps.setObject(1, book.getIsbn());
                    ps.setObject(2, book.getTitle());
                    ps.setObject(3, book.getAuthor());
                    ps.setObject(4, book.getPrice());
                    ps.setObject(5, book.getPublished_at());
                    ps.executeUpdate();
                    return book;
                });
        return result;
    }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public Book findById(Integer id) {
        final RowMapper<Book> mapper = new BeanPropertyRowMapper<>(Book.class);
        return jdbcTemplate.queryForObject("SELECT * from book WHERE id = ?", mapper, id);
    }
}
