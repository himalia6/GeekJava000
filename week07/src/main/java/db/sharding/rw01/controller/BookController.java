package db.sharding.rw01.controller;

import com.google.common.net.MediaType;
import db.sharding.rw01.domain.Book;
import db.sharding.rw01.service.BookService;
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
