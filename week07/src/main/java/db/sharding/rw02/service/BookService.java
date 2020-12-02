package db.sharding.rw02.service;

import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BookService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public void insert() {
        jdbcTemplate.execute("INSERT INTO t_order (user_id, status) VALUES (?, ?)", (PreparedStatementCallback<Object>) ps -> {
            ps.setObject(1, i);
            ps.setObject(2, "init");
            ps.executeUpdate();
        });
    }
}
