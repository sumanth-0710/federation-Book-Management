package com.graphql.fedration.fedration_book.service;

import com.graphql.fedration.fedration_book.dto.BookDto;
import com.graphql.fedration.fedration_book.dto.BookInputDto;
import com.graphql.fedration.fedration_book.dto.BookUpdateDto;
import com.graphql.fedration.fedration_book.exception.AuthorNotFoundException;
import com.graphql.fedration.fedration_book.exception.BookNotFoundException;
import com.graphql.fedration.fedration_book.loader.BookDataLoader;
import com.graphql.fedration.fedration_book.mapper.BookMapper;
import com.graphql.fedration.fedration_book.model.Author;
import com.graphql.fedration.fedration_book.model.Book;
import com.graphql.fedration.fedration_book.repository.AuthorRepo;
import com.graphql.fedration.fedration_book.repository.BookRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final BookDataLoader bookDataLoader;

    public BookService(BookRepo bookRepo, AuthorRepo authorRepo, BookDataLoader bookDataLoader) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.bookDataLoader = bookDataLoader;
    }

    // -------------------
    // Async fetch for Apollo Federation
    // -------------------
    public CompletableFuture<BookDto> getByIdAsync(Long id) {
        return CompletableFuture.supplyAsync(() ->
                bookRepo.findById(id)
                        .map(BookMapper::toDto)
                        .orElseThrow(() -> new BookNotFoundException("Book not found"))
        );
    }

    // -------------------
    // CRUD operations
    // -------------------
    public BookDto getById(Long id) {
        return bookRepo.findById(id)
                .map(BookMapper::toDto)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    public List<BookDto> getAll() {
        return bookRepo.findAll().stream()
                .map(BookMapper::toDto) // Author is not eagerly fetched here; resolved via @SchemaMapping
                .collect(Collectors.toList());
    }

    public BookDto add(BookInputDto input) {
        Author author = authorRepo.findById(input.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
        Book book = BookMapper.toEntity(input, author);
        return BookMapper.toDto(bookRepo.save(book));
    }

    public BookDto update(BookUpdateDto input) {
        Book book = bookRepo.findById(input.getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        if (input.getTitle() != null) book.setTitle(input.getTitle());
        if (input.getPublishedYear() != null) book.setPublishedYear(input.getPublishedYear());
        if (input.getAuthorId() != null) {
            Author author = authorRepo.findById(input.getAuthorId())
                    .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
            book.setAuthor(author);
        }
        return BookMapper.toDto(bookRepo.save(book));
    }

    public boolean delete(Long id) {
        if (!bookRepo.existsById(id)) return false;
        bookRepo.deleteById(id);
        return true;
    }

    public CompletableFuture<List<Book>> getBooksByIds(List<Long> ids) {
        return bookDataLoader.loadBooks(ids);
    }
}
