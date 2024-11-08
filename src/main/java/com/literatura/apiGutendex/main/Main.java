package com.literatura.apiGutendex.main;

import com.literatura.apiGutendex.model.Author;
import com.literatura.apiGutendex.model.Book;
import com.literatura.apiGutendex.model.BookData;
import com.literatura.apiGutendex.model.Data;
import com.literatura.apiGutendex.repository.IAuthorRepository;
import com.literatura.apiGutendex.repository.IBookRepository;
import com.literatura.apiGutendex.service.ConvertData;
import com.literatura.apiGutendex.service.FetchApi;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/* TO DO
 * 1 - buscar libro por titulo (api)
 * 2 - listar libros registrados (bd)
 * 3 - listar autores registrados (bd)
 * 4 - listar autores vivos en un determinado año (bd)
 * 5 - listar libros por idioma (bd)
 * */

public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final String URL = "https://gutendex.com/books/?search=";
    private final FetchApi fetch = new FetchApi();
    private final ConvertData convertData = new ConvertData();
    private final IBookRepository bookRepository;
    private final IAuthorRepository authorRepository;
    List<Book> books;
    List<Author> authors;

    public Main(IBookRepository bookRepository, IAuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void start() {
        var opt = -1;
        while (opt != 0) {
            var menu = """
                    \n>===Select an option===<
                    
                    1 - Search book by title
                    2 - List registered books
                    3 - List registered authors
                    4 - List authors alive in a given year
                    5 - List books by language
                    0 - Exit
                    
                    >===                  ===<
                    """;

            System.out.println(menu);
            try {
                opt = sc.nextInt();

            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
            sc.nextLine();

            switch (opt) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listAuthorsByYear();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 0:
                    System.out.println("Closing the app...");
                    break;
                default:
                    System.out.println("Invalid option");
            }

        }

    }

    private void searchBookByTitle() {
        System.out.print("Enter the book's name you want to search for: ");
        String bookTitle = sc.nextLine().toLowerCase();

        if (!isValidTitle(bookTitle)) {
            System.out.println("\nYour search must be greater than 3 characters!");
        } else {
            Optional<Book> existingBook = findBookInDatabase(bookTitle);
            if (existingBook.isPresent()) {
                System.out.println("\nBook already exists in the database: " + existingBook.get());
            } else {
                fetchAndAddBookFromApi(encodeForUrl(bookTitle));
            }
        }
    }

    private Optional<Book> findBookInDatabase(String bookTitle) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(bookTitle))
                .findFirst();
    }

    private void fetchAndAddBookFromApi(String bookTitle) {
        String json = fetch.fetchApi(URL + bookTitle);
        List<BookData> data = convertData.convertData(json, Data.class).bookDataList();

        if (data.isEmpty()) {
            System.out.println("\nNot found in the API");
            return;
        }

        Optional<BookData> auxBook = data.stream()
                .filter(b -> containsTitle(b.title(), decodeForUrl(bookTitle)))
                .findFirst();

        if (auxBook.isEmpty()) {
            System.out.println("\nNot found in the API");
            return;
        }

        Book book = new Book(auxBook.get());
        associateAndSaveBook(book);
        System.out.println("\nBook added to the database: " + book);
    }

    private void associateAndSaveBook(Book book) {
        Author author = new Author(
                book.getAuthor().getName(),
                book.getAuthor().getBirthYear(),
                book.getAuthor().getDeathYear()
        );

        authorRepository.findByNameAndBirthYearAndDeathYear(
                author.getName(), author.getBirthYear(), author.getDeathYear()
        ).ifPresentOrElse(
//                before
//                existingAuthor -> book.setAuthor(existingAuthor),
//                now
                book::setAuthor,
                () -> {
                    authorRepository.save(author);
                    book.setAuthor(author);
                }
        );

        bookRepository.save(book);
    }

    private void listRegisteredBooks() {
        books = bookRepository.findAll();
        displayBooks(books);
    }

    private void listRegisteredAuthors() {
        authors = authorRepository.findAll();
        displayAuthors(authors);
    }

    private void listAuthorsByYear() {
        System.out.print("Enter the year alive of the author you want to search: ");
        int year = 0;

        try {
            year = sc.nextInt();

        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
        sc.nextLine();

        authors = authorRepository.findAuthorsByGivenYear(year);

        if (authors.isEmpty()) {
            System.out.println("\nAuthors not found");
        } else {
            displayAuthors(authors);
        }
    }

    private void listBooksByLanguage() {
        System.out.println("\nEnter the language to search for the books: ");
        System.out.println("""
                es - Spanish
                en - English
                fr - French
                pt - Portuguese
                """);
        String language = sc.nextLine().toLowerCase();

        List<Book> booksByLang = bookRepository.findByLanguage(language);
        if (booksByLang.isEmpty()) {
            System.out.println("Books not found");
        } else {
            displayBooks(booksByLang);
        }
    }

    private boolean containsTitle(String title, String searchedTitle) {
        String normalizedTitle = title.toLowerCase();
        String normalizedSearchedTitle = searchedTitle.toLowerCase();

        return normalizedTitle.contains(normalizedSearchedTitle);
    }

    private void displayAuthors(List<Author> authors) {
        authors
                .forEach(a -> {
                    System.out.println("\n----- Book -----");
                    System.out.println("Name: " + a.getName());
                    System.out.println("Birth day: " + a.getBirthYear());
                    System.out.println("Death year: " + a.getDeathYear());
                    System.out.print("Books: ");
                    List<String> titleList = a.getBooks().stream().map(Book::getTitle).toList();
                    System.out.print(String.join(", ", titleList));
                    System.out.println("\n----- ---- -----");
                });
    }

    private void displayBooks(List<Book> books) {
        books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(b -> {
                    System.out.println("\n----- Book -----");
                    System.out.println("Title: " + b.getTitle());
                    System.out.println("Author: " + b.getAuthor().getName());
                    System.out.println("Language: " + b.getLanguage());
                    System.out.println("Downloads: " + b.getDownloads());
                    System.out.println("----- ---- -----");
                });
    }

    private boolean isValidTitle(String title) {
        return title.length() >= 3;
    }

    private String encodeForUrl(String title) {
        return URLEncoder.encode(title, StandardCharsets.UTF_8);
    }

    private String decodeForUrl(String title) {
        return URLDecoder.decode(title, StandardCharsets.UTF_8);
    }
}
