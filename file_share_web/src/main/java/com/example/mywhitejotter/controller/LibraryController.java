package com.example.mywhitejotter.controller;

import com.example.mywhitejotter.pojo.Book;
import com.example.mywhitejotter.pojo.User;
import com.example.mywhitejotter.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class LibraryController {
    @Autowired
    BookService bookService;

    @CrossOrigin
    @GetMapping("/api/books")
    public List<Book> list() {
        return bookService.list();
    }

    @CrossOrigin
    @PostMapping("/api/books")
    public Book addOrUpdate(@RequestBody Book book, HttpSession session) {
        User user;
        if (session != null && (user = (User)session.getAttribute("user")) != null) {
            book.setUser(user);
            System.out.println("get the user is: " + user);
        }
        bookService.addOrUpdate(book);
        return book;
    }

    @CrossOrigin
    @PostMapping("/api/delete")
    public void delete(@RequestBody Book book, HttpSession session) {
        User user;
        if (session != null && (user = (User)session.getAttribute("user")) != null) {

            if (user.getPermission() == 0 || user.getId() == book.getUser().getId()) {
                System.out.println("user_id: " + user.getId() + " 删除书籍: " + book.toString());
                bookService.deleteById(book.getId());
            } else {
                System.out.println("权限不够!");
            }
        }

    }

    @CrossOrigin
    @GetMapping("/api/categories/{cid}/books")
    public List<Book> listByCategory(@PathVariable("cid") int cid) {
        if (0 != cid) {
            return bookService.listByCategory(cid);
        } else {
            return list();
        }
    }

    @CrossOrigin
    @GetMapping("/api/user/{uid}/books")
    public List<Book> listByUser(@PathVariable("uid") int uid) {
        return bookService.listByUser(uid);
    }

}
