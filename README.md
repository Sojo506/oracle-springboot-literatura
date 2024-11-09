# LiterAlura: Gutendex API

## Menú de inicio

#>===Select an option===<

1 - [Search book by title](#search-book-by-title)
2 - [List registered books](#list-registered-books)
3 - [List registered authors](#list-registered-authors)
4 - [List authors alive in a given year](#list-authors-alive-in-a-given-year)
5 - [List books by language](#list-books-by-language)
0 - [Exit](#exit)

#>===                  ===<


![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.4-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13-blue)
![Maven](https://img.shields.io/badge/Maven-3.6.3-orange)

## Índice
1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [Estado del Proyecto](#estado-del-proyecto)
3. [Demostración de Funciones y Aplicaciones](#demostración-de-funciones-y-aplicaciones)
4. [Acceso al Proyecto](#acceso-al-proyecto)
5. [Tecnologías Utilizadas](#tecnologías-utilizadas)
6. [Personas Contribuyentes](#personas-contribuyentes)
7. [Personas Desarrolladoras del Proyecto](#personas-desarrolladoras-del-proyecto)
8. [Licencia](#licencia)

## Descripción del Proyecto
Este proyecto es un reto llamado **LiterAlura**, que consiste en una aplicación Java que interactúa con la API de Gutendex para buscar libros, listar libros y autores registrados, y gestionar sus datos.

## Estado del Proyecto
El proyecto está en desarrollo activo, implementando nuevas funcionalidades y mejorando la gestión de datos y la interfaz de usuario.

## Demostración de Funciones y Aplicaciones
### Menú Interactivo
- **Buscar libro por título**: Ingresa el título del libro que deseas buscar.
- **Listar libros registrados**: Muestra una lista de todos los libros en la base de datos.
- **Listar autores registrados**: Muestra una lista de todos los autores en la base de datos.
- **Listar autores vivos en un año dado**: Ingresa un año para buscar autores que estaban vivos en ese año.
- **Listar libros por idioma**: Ingresa el idioma (es, en, fr, pt) para listar los libros en ese idioma.
- **Salir**: Cierra la aplicación.

### Código de Ejemplo
```java
// Ejemplo de cómo iniciar la aplicación y buscar un libro por título
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        Main mainApp = context.getBean(Main.class);
        mainApp.start();
    }
}
```

## Acceso al Proyecto
1. Clona el repositorio:

    ```bash
    git clone https://github.com/tu-usuario/literalura-gutendex-api.git
    cd literalura-gutendex-api
    ```

2. Configura la base de datos en `application.properties`:

    ```properties
    spring.application.name=apiGutendex

    spring.datasource.url=jdbc:postgresql://${DB_HOST}/api_gutendex
    spring.datasource.username=${DB_USER}
    spring.datasource.password=${DB_PASSWORD}
    spring.datasource.driver-class-name=org.postgresql.Driver
    hibernate.dialect=org.hibernate.dialect.HSQLDialect

    spring.jpa.hibernate.ddl-auto=update

    spring.jpa.show-sql=true
    spring.jpa.format-sql=true
    ```

3. Compila el proyecto:

    ```bash
    mvn clean install
    ```

4. Ejecuta la aplicación:

    ```bash
    mvn exec:java -Dexec.mainClass="com.literatura.apiGutendex.main.Main"
    ```

## Tecnologías Utilizadas
- **Java 11**
- **Spring Boot**
- **PostgreSQL**
- **Maven**
- **Jackson**

### Dependencias
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.17.2</version>
    </dependency>
</dependencies>
```

## Personas Contribuyentes
- **Fabián Sojo **: [GitHub](https://github.com/Sojo506)

## Personas Desarrolladoras del Proyecto
- **Fabián Sojo **: [GitHub](https://github.com/Sojo506)
