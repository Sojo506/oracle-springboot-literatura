package com.literatura.apiGutendex.main;

import com.literatura.apiGutendex.service.FetchApi;

import java.util.Scanner;

/* TO DO
 * 1 - buscar libro por titulo (api)
 * 2 - listar libros registrados (bd)
 * 3 - listar autores registrados (bd)
 * 4 - listar autores vivos en un determinado año (bd)
 * 5 - listar libros por idioma (bd)
 * */

public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final String URL = "https://gutendex.com/books?";
    private final FetchApi fetchApi = new FetchApi();

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
            opt = sc.nextInt();
            sc.nextLine();

            switch (opt) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 0:
                    System.out.println("Closing the app...");
                    break;
                default:
                    System.out.println("Invalid option");
            }

        }

    }
}
