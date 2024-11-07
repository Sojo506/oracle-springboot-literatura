package com.literatura.apiGutendex.repository;

import com.literatura.apiGutendex.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookRepository extends JpaRepository<Book, Long> {
}
