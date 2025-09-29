package com.graphql.fedration.fedration_book.resolver;


import com.graphql.fedration.fedration_book.dto.AuthorDto;
import com.graphql.fedration.fedration_book.dto.AuthorInputDto;
import com.graphql.fedration.fedration_book.mapper.AuthorMapper;
import com.graphql.fedration.fedration_book.model.Author;
import com.graphql.fedration.fedration_book.model.Book;
import com.graphql.fedration.fedration_book.service.AuthorService;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.federation.EntityMapping;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class AuthorResolver {

    private final AuthorService service;

    public AuthorResolver(AuthorService service) {
        this.service = service;
    }

    // ---------------------
    // Queries
    // ---------------------
    @QueryMapping
    public AuthorDto getAuthorById(@Argument Long id) {
        return service.getById(id);
    }

    @QueryMapping
    public List<AuthorDto> getAllAuthors() {
        return service.getAll();
    }

    // ---------------------
    // Mutations
    // ---------------------
    @MutationMapping
    public AuthorDto addAuthor(@Argument AuthorInputDto input) {
        return service.add(input);
    }

   /* @MutationMapping
    public AuthorDto updateAuthor(@Argument AuthorUpdateDto input) {
        return service.update(input);
    }*/

    @MutationMapping
    public Boolean deleteAuthor(@Argument Long id) {
        return service.delete(id);
    }

    // ---------------------
    // Federation entity resolver
    // ---------------------
    @EntityMapping(name = "Book")
    public CompletableFuture<AuthorDto> resolveAuthor(Book book, DataLoader<Long, Author> authorLoader) {
        return authorLoader.load(book.getAuthor().getId())
                .thenApply(author -> AuthorMapper.toDto(author, false));
    }

}
