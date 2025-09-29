package com.graphql.fedration.fedration_book.repository;

import com.graphql.fedration.fedration_book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
}
