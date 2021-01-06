package cluster.redis.controller;

import cluster.redis.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class BookController {
    @Autowired
    private Jedis jedis;

    @GetMapping("/books/{title}")
    public Book find(@PathVariable("title") String title) {
        final Map<String, String> data = jedis.hgetAll(title);
        final Book book = new Book();
        if (data.containsKey("author"))
            book.setAuthor(data.get("author"));
        if (data.containsKey("title"))
            book.setTitle(data.get("title"));
        if (data.containsKey("price"))
            book.setPrice(new BigDecimal(data.get("price")));
        return book;
    }

    @PostMapping("/books")
    public String enroll(@RequestBody Book book) {
        if(book.getTitle() != null && !book.getTitle().equals("")) {
            jedis.hset(book.getTitle(), book.toStringMap());
            return book.getTitle();
        }
        return "Malformed Data";
    }
}
