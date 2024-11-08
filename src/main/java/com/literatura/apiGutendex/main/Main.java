package com.literatura.apiGutendex.main;

import com.literatura.apiGutendex.model.Author;
import com.literatura.apiGutendex.model.Book;
import com.literatura.apiGutendex.model.BookData;
import com.literatura.apiGutendex.model.Data;
import com.literatura.apiGutendex.repository.IAuthorRepository;
import com.literatura.apiGutendex.repository.IBookRepository;
import com.literatura.apiGutendex.service.ConvertData;
import com.literatura.apiGutendex.service.FetchApi;

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
    List<Book> books;
    List<Author> authors;
    private IBookRepository bookRepository;
    private IAuthorRepository authorRepository;

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
                    System.out.print("Enter the book's name you want to search for: ");
                    String bookTitle = sc.nextLine().toLowerCase().replace(" ", "%20");

                    // search book in the db
                    Optional<Book> existingBook = bookRepository.findAll().stream()
                            .filter(b -> b.getTitle().toLowerCase().contains(bookTitle))
                            .findFirst();

                    if (existingBook.isPresent()) {
                        System.out.println("Book already exists in the database: " + existingBook.get());
                    } else {
                        // Fetch data from API si no está en la base de datos
                        String json = fetch.fetchApi(URL + bookTitle);
                        List<BookData> data = convertData.convertData(json, Data.class).bookDataList();

                        if (data.isEmpty()) {
                            System.out.println("Not found in the API");
                            break;
                        }

                        // Find the book by title in the API response
                        Optional<BookData> auxBook = data.stream()
                                .filter(b -> containsTitle(b.title(), bookTitle))
                                .findFirst();

                        if (auxBook.isEmpty()) {
                            System.out.println("Not found in the API");
                            break;
                        }

                        Book book = new Book(auxBook.get());

                        // create or find author, then associate it with the book
                        Author author = new Author(
                                book.getAuthor().getName(),
                                book.getAuthor().getBirthYear(),
                                book.getAuthor().getDeathYear()
                        );

                        authorRepository.findByNameAndBirthYearAndDeathYear(
                                author.getName(), author.getBirthYear(), author.getDeathYear()
                        ).ifPresentOrElse(existingAuthor -> {
                            book.setAuthor(existingAuthor);
                        }, () -> {
                            authorRepository.save(author);
                            book.setAuthor(author);
                        });

                        // save book in the db
                        bookRepository.save(book);
                        System.out.println("Book added to the database: " + book);
                    }
                    break;
                case 2:
                    books = bookRepository.findAll();

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
                    break;
                case 3:
                    authors = authorRepository.findAll();

                    authors.stream()
                            .forEach(a -> {
                                System.out.println("\n----- Author -----");
                                System.out.println("Name: " + a.getName());
                                System.out.println("Birth day: " + a.getBirthYear());
                                System.out.println("Death year: " + a.getDeathYear());
                                System.out.print("Books: ");
                                List<String> titleList = a.getBooks().stream().map(Book::getTitle).toList();
                                System.out.print(String.join(", ", titleList));

                                System.out.println("\n----- ---- -----");
                            });
                    break;
                case 4:
                    System.out.print("Enter the year alive of the author you want to search: ");
                    Integer year = sc.nextInt();
                    sc.nextLine();

                    authors = authorRepository.findAuthorsByGivenYear(year);

                    if (authors.isEmpty()) {
                        System.out.println("Authors not found");
                    } else {
                        authors.stream()
                                .forEach(a -> {
                                    System.out.println();
                                    System.out.println("Name: " + a.getName());
                                    System.out.println("Birth day: " + a.getBirthYear());
                                    System.out.println("Death year: " + a.getDeathYear());
                                    System.out.print("Books: ");
                                    List<String> titleList = a.getBooks().stream().map(Book::getTitle).toList();
                                    System.out.print(String.join(", ", titleList));
                                    System.out.println();
                                });
                    }
                    break;
                case 5:
                    System.out.print("Enter the language to search for the books: ");
                    System.out.println("""
                            es - spanish
                            en - english
                            fr - france
                            pt - portuguese
                            """);
                    var searchBookByLang = sc.nextLine().toLowerCase();

                    List<Book> booksByLang = bookRepository.findByLanguage(searchBookByLang);

                    if (booksByLang.isEmpty()) {
                        System.out.println("Books not found");
                    } else {
                        booksByLang.stream()
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
                    break;
                case 0:
                    System.out.println("Closing the app...");
                    break;
                default:
                    System.out.println("Invalid option");
            }

        }

    }

    private boolean containsTitle(String title, String searchedTitle) {
        List<String> words = List.of(title.toLowerCase().split("\\s"));

        for (String word : words) {
            if (word.equals(searchedTitle.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
