package com.graphql.fedration.fedration_book.repository;

import com.graphql.fedration.fedration_book.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepo extends JpaRepository<Author, Long> {
}
