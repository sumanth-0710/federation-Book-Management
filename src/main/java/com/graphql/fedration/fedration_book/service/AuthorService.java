package com.graphql.fedration.fedration_book.service;

import com.graphql.fedration.fedration_book.dto.AuthorDto;
import com.graphql.fedration.fedration_book.dto.AuthorInputDto;
import com.graphql.fedration.fedration_book.exception.AuthorNotFoundException;
import com.graphql.fedration.fedration_book.loader.AuthorDataLoader;
import com.graphql.fedration.fedration_book.mapper.AuthorMapper;
import com.graphql.fedration.fedration_book.model.Author;
import com.graphql.fedration.fedration_book.repository.AuthorRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepo repo;
    private final AuthorDataLoader authorDataLoader;

    public AuthorService(AuthorRepo repo, AuthorDataLoader authorDataLoader) {
        this.repo = repo;
        this.authorDataLoader = authorDataLoader;
    }

    // -------------------
    // Async fetch for Apollo Federation (single author)
    // -------------------
    public CompletableFuture<AuthorDto> getByIdAsync(Long id) {
        return authorDataLoader.loadAuthors(List.of(id)) // batch load single ID
                .thenApply(authors -> {
                    Author author = authors.get(0);
                    if (author == null) {
                        throw new AuthorNotFoundException("Author not found");
                    }
                    return AuthorMapper.toDto(author, false);
                });
    }

    // -------------------
    // Async fetch multiple authors (batch)
    // -------------------
    public CompletableFuture<List<AuthorDto>> getByIdsAsync(List<Long> ids) {
        return authorDataLoader.loadAuthors(ids)
                .thenApply(authors -> authors.stream()
                        .map(author -> AuthorMapper.toDto(author, false))
                        .collect(Collectors.toList())
                );
    }

    // -------------------
    // CRUD operations
    // -------------------
    public AuthorDto getById(Long id) {
        return repo.findById(id)
                .map(a -> AuthorMapper.toDto(a, true))
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
    }

    public List<AuthorDto> getAll() {
        return repo.findAll().stream()
                .map(a -> AuthorMapper.toDto(a, true))
                .collect(Collectors.toList());
    }

    public AuthorDto add(AuthorInputDto input) {
        Author author = AuthorMapper.toEntity(input);
        return AuthorMapper.toDto(repo.save(author), true);
    }

    public boolean delete(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

}