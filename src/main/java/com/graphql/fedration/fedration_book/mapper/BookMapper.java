package com.graphql.fedration.fedration_book.mapper;

import com.graphql.fedration.fedration_book.dto.BookDto;
import com.graphql.fedration.fedration_book.dto.BookInputDto;
import com.graphql.fedration.fedration_book.model.Author;
import com.graphql.fedration.fedration_book.model.Book;

public class BookMapper {
    public static BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setPublishedYear(book.getPublishedYear());
        if (book.getAuthor() != null) {
            dto.setAuthor(AuthorMapper.toDto(book.getAuthor(), false));
        }
        return dto;
    }
    public static Book toEntity(BookInputDto dto, Author author) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setPublishedYear(dto.getPublishedYear());
        book.setAuthor(author);
        return book;
    }
}
