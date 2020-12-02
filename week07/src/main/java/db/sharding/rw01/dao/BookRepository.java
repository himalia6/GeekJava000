package db.sharding.rw01.dao;

import db.sharding.rw01.domain.Book;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
}
