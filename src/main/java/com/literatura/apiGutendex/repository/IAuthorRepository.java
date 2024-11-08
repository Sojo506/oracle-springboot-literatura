package com.literatura.apiGutendex.repository;

import com.literatura.apiGutendex.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameAndBirthYearAndDeathYear(String name, Integer birthYear, Integer deathYear);

    @Query("SELECT a FROM Author a WHERE a.birthYear <= :year AND a.deathYear >= :year")
    List<Author> findAuthorsByGivenYear(Integer year);

}
