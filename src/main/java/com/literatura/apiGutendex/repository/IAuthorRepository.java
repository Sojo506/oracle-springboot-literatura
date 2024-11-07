package com.literatura.apiGutendex.repository;

import com.literatura.apiGutendex.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorRepository extends JpaRepository<Author, Long> {
}
