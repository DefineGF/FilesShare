package com.example.mywhitejotter.dao;

import com.example.mywhitejotter.pojo.Book;
import com.example.mywhitejotter.pojo.BookCategory;
import com.example.mywhitejotter.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDAO extends JpaRepository<Book, Integer> {
    List<Book> findAllByBookCategory(BookCategory category);
    List<Book> findAllByUser(User user);
    List<Book> findAllByNameLikeOrAuthorLike(String keyword1, String keyword2);
}
