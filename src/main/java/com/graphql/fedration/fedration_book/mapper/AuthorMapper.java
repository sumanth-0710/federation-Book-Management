package com.graphql.fedration.fedration_book.mapper;

import com.graphql.fedration.fedration_book.dto.AuthorDto;
import com.graphql.fedration.fedration_book.dto.AuthorInputDto;
import com.graphql.fedration.fedration_book.model.Author;

import java.util.stream.Collectors;

public class AuthorMapper {
    public static AuthorDto toDto(Author author, boolean includeBooks) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setPhoneNumber(author.getPhoneNumber());
        dto.setAddress(author.getAddress());
        if (includeBooks) {
            dto.setBooks(author.getBooks().stream()
                    .map(BookMapper::toDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
    public static Author toEntity(AuthorInputDto dto) {
        Author author = new Author();
        author.setName(dto.getName());
        author.setPhoneNumber(dto.getPhoneNumber());
        author.setAddress(dto.getAddress());
        return author;
    }
}