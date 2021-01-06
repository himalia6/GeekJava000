package cluster.redis.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class Book {
    private String title;

    private String author;

    private BigDecimal price;

    public Map<String, String> toStringMap() {
        final Map<String, String> result = new HashMap<>();
        if (this.getTitle() != null) result.put("title", title);
        if (this.getAuthor() != null) result.put("author", author);
        if (this.getPrice() != null) result.put("price", price.toPlainString());
        return result;
    }
}
