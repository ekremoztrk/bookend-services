package com.bookend.bookservice.controller;

import com.bookend.bookservice.model.Book;
import com.bookend.bookservice.model.Genre;
import com.bookend.bookservice.payload.BookRequest;
import com.bookend.bookservice.service.BookService;
import com.bookend.bookservice.service.GenreService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private BookService bookService;
    @Autowired
    public void setBookService(BookService bookService){
        this.bookService=bookService;
    }
    private GenreService genreService;
    @Autowired
    public void setGenreService(GenreService genreService) {
        this.genreService = genreService;
    }

    @ApiOperation(value = "Get the book by Id", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book"),
            @ApiResponse(code = 401, message = "You are not authorized to view the book"),
            @ApiResponse(code = 404, message = "Book is not found.")
    })
    @GetMapping("/{bookid}")
    public Book getBookInfo(@PathVariable("bookid") String bookId ) {

        Book book = bookService.getById(bookId);
        if(book==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The book does not exist.");
        }
        return book;

    }
    @ApiOperation(value = "Search book or get all books", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book list"),
            @ApiResponse(code = 401, message = "You are not authorized to search book"),
            @ApiResponse(code = 404, message = "Book is not found for given title.")
    })
    @GetMapping("")
    public List<Book> search(@RequestParam(required = false) String title
            ,@RequestParam(required = false) String genre){
        List<Book> books = new ArrayList<Book>();

        if(title == null){
            bookService.getAll().forEach(books::add);

        }
        else {
            bookService.search(title).forEach(books::add);
            if(books==null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no match for title.");
            }
        }
        if(genre!=null){
            books = books.stream().filter(book ->
                    book.getGenre().getGenre().equals(genre))
                    .collect(Collectors.toList());
            if(books==null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no match.");
            }
        }

        return books;
    }
    @ApiOperation(value = "Get books in the shelf", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book list"),
            @ApiResponse(code = 401, message = "You are not authorized to view books.")
    })
    @GetMapping("/user/shelf/{shelfid}")
    public List<Book> getBooksofShelf(@PathVariable("shelfid") String shelfID, OAuth2Authentication auth){
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();

        String accessToken = details.getTokenValue();
        return bookService.getBooksofShelf(Long.valueOf(shelfID),accessToken);
    }
    @ApiOperation(value = "Get books of a specific author", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book list"),
            @ApiResponse(code = 401, message = "You are not authorized to view books.")
    })
    @GetMapping("/author/{authorid}")
    public List<Book> getBookOfAuthor(@PathVariable("authorid") String authorId){
        return bookService.findByAuthor(authorId);
    }
    @ApiOperation(value = "Add new book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added book"),
            @ApiResponse(code = 401, message = "You are not authorized to add the resource"),
            @ApiResponse(code = 400, message = "The way you are trying to add book is not accepted.")
    }
    )
    @PostMapping("/new")
    public Book userBook(@RequestBody BookRequest bookRequest){
        Book book= new Book();
        if(bookRequest.getBookName()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book name field cannot be empty.");
        }
        book.setBookName(bookRequest.getBookName());
        if(bookRequest.getAuthor()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Author field cannot be empty.");
        }
        book.setAuthor(bookRequest.getAuthor());
        book.setDescription(bookRequest.getDescription());
        Genre genre = genreService.findByGenre(bookRequest.getGenre());
        if(genre == null){
            genre = genreService.addNewGenre(bookRequest.getGenre());
        }
        book.setGenre(genre);
        book.setAuthorid(bookRequest.getAuthorid());
        book.setPage(bookRequest.getPage());
        book.setVerified(Boolean.FALSE);
        book.setISBN(bookRequest.getISBN());
        Book addedBook =bookService.saveOrUpdate(book);
        if(addedBook==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book already exists.");
        }
        return addedBook;
    }


}
