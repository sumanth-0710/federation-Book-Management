package com.graphql.fedration.fedration_book.fetcher;

import com.graphql.fedration.fedration_book.model.Book;
import com.graphql.fedration.fedration_book.repository.BookRepo;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class BookFetcher {
    private final BookRepo repo;

    public BookFetcher(BookRepo repo) {
        this.repo = repo;
    }

    public CompletableFuture<Book> getBook(Long id) {
        return CompletableFuture.supplyAsync(() -> repo.findById(id).orElse(null));
    }
}