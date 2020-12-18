package db.sharding.common.dao;

import db.sharding.common.domain.Book;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
}
