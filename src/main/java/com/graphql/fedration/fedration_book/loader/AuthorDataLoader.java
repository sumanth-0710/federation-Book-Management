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

    public CompletableFuture<List<Author>> loadAuthors(List<Long> ids) {
        return CompletableFuture.supplyAsync(() -> {
            List<Author> authors = authorRepo.findAllById(ids);
            Map<Long, Author> map = authors.stream()
                    .collect(Collectors.toMap(Author::getId, a -> a));
            return ids.stream()
                    .map(id -> map.getOrDefault(id, null)) // safely return null if missing
                    .collect(Collectors.toList());
        });
    }


}
