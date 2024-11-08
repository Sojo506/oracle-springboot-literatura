package com.literatura.apiGutendex.repository;

import com.literatura.apiGutendex.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IBookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

}
