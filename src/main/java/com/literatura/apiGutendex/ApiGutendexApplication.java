package com.literatura.apiGutendex;

import com.literatura.apiGutendex.main.Main;
import com.literatura.apiGutendex.repository.IAuthorRepository;
import com.literatura.apiGutendex.repository.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGutendexApplication implements CommandLineRunner {

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IAuthorRepository authorRepository;

    public static void main(String[] args) {
        SpringApplication.run(ApiGutendexApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Main main = new Main(bookRepository, authorRepository);
        main.start();
    }
}
