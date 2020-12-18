package db.sharding.rw02.controller;

import db.sharding.common.domain.Book;
import db.sharding.rw02.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable("id") Integer id) {
        return bookService.findById(id);
    }

    @PostMapping
    public Book register(@RequestBody Book book) {
        return bookService.save(book);
    }
}
