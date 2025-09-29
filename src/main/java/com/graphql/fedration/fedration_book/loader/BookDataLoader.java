package com.graphql.fedration.fedration_book.loader;

import com.graphql.fedration.fedration_book.model.Book;
import com.graphql.fedration.fedration_book.repository.BookRepo;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class BookDataLoader {

    private final BookRepo bookRepo;

    public BookDataLoader(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    // Batch loader that returns CompletableFuture<List<Book>>
    public CompletableFuture<List<Book>> loadBooks(List<Long> ids) {
        return CompletableFuture.supplyAsync(() -> {
            List<Book> books = bookRepo.findAllById(ids);
            Map<Long, Book> bookMap = books.stream()
                    .collect(Collectors.toMap(Book::getId, b -> b));

            return ids.stream()
                    .map(bookMap::get)
                    .collect(Collectors.toList());
        });
    }

}
