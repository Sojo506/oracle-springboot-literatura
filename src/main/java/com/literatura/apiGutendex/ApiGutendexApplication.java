package com.literatura.apiGutendex;

import com.literatura.apiGutendex.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGutendexApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ApiGutendexApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Main main = new Main();
        main.start();
    }
}
