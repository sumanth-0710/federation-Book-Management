package com.graphql.fedration.fedration_book.resolver;


import com.graphql.fedration.fedration_book.dto.BookDto;
import com.graphql.fedration.fedration_book.dto.BookInputDto;
import com.graphql.fedration.fedration_book.dto.BookUpdateDto;
import com.graphql.fedration.fedration_book.service.BookService;
import org.springframework.graphql.data.federation.EntityMapping;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Map;

@Controller
public class BookResolver {

    private final BookService service;

    public BookResolver(BookService service) {
        this.service = service;
    }

    // ---------------------
    // Queries
    // ---------------------
    @QueryMapping
    public BookDto getBookById(@Argument Long id) {
        return service.getById(id);
    }

    @QueryMapping
    public List<BookDto> getAllBooks() {
        return service.getAll();
    }

    // ---------------------
    // Mutations
    // ---------------------
    @MutationMapping
    public BookDto addBook(@Argument BookInputDto input) {
        return service.add(input);
    }

    @MutationMapping
    public BookDto updateBook(@Argument BookUpdateDto input) {
        return service.update(input);
    }

    @MutationMapping
    public Boolean deleteBook(@Argument Long id) {
        return service.delete(id);
    }

    // ---------------------
    // Federation entity resolver
    // ---------------------
    @EntityMapping(name = "Book")
    public BookDto resolveBook(Map<String, Object> reference) {
        Long id = Long.parseLong(reference.get("id").toString());
        return service.getById(id);
    }
}
