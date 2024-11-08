package com.literatura.apiGutendex.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;


@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private Author author;

    private String language;
    private Integer downloads;

    public Book() {
    }

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

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author=" + (author != null ? author.getName() : "Unknown") +
                ", language='" + language + '\'' +
                ", downloads=" + downloads +
                '}';
    }
}
