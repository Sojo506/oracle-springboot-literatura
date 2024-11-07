package com.literatura.apiGutendex.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;


@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private Author author;

    private String language;
    private Integer downloads;

    public Book(BookData bookData) {
        this.title = bookData.title();
        bookData.authors().forEach(authorData -> {
            Author author = new Author(authorData.name(), authorData.birth_year(), authorData.death_year());
            this.author = author;
        });
        this.language = bookData.languages().isEmpty() ? "Unknown" : bookData.languages().get(0);
        this.downloads = bookData.download_count();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getDownloads() {
        return downloads;
    }

    @Override
    public String toString() {
        return "Book{" +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", language='" + language + '\'' +
                ", downloads=" + downloads +
                '}';
    }
}
