package com.graphql.fedration.fedration_book.loader;

import com.graphql.fedration.fedration_book.model.Author;
import com.graphql.fedration.fedration_book.repository.AuthorRepo;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class AuthorDataLoader {

    private final AuthorRepo authorRepo;

    public AuthorDataLoader(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    // Batch loader that returns CompletableFuture<List<Author>>
    public CompletableFuture<List<Author>> loadAuthors(List<Long> ids) {
        return CompletableFuture.supplyAsync(() -> {
            // Fetch all authors in one query
            List<Author> authors = authorRepo.findAllById(ids);

            // Map authors by ID for easy lookup
            Map<Long, Author> authorMap = authors.stream()
                    .collect(Collectors.toMap(Author::getId, a -> a));

            // Return authors in the same order as requested IDs
            return ids.stream()
                    .map(authorMap::get)
                    .collect(Collectors.toList());
        });
    }

}
