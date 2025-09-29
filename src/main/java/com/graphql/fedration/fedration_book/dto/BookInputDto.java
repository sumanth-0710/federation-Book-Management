package com.graphql.fedration.fedration_book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookInputDto {
    private String title;
    private int publishedYear;
    private Long authorId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    // getters and setters
}

