package com.example.mywhitejotter.service;

import com.example.mywhitejotter.dao.BookDAO;
import com.example.mywhitejotter.pojo.Book;
import com.example.mywhitejotter.pojo.BookCategory;
import com.example.mywhitejotter.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    BookDAO bookDAO;

    @Autowired
    BookCategoryService categoryService;

    @Autowired
    UserService userService;

    public List<Book> list() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return bookDAO.findAll(sort);
    }

    public void addOrUpdate(Book book) {
        bookDAO.save(book);   // 当 主键 存在时更新数据； 主键 不存在时 插入数据；
    }

    public void deleteById(int id) {
        bookDAO.deleteById(id);
    }

    public Book get(int bid) {
        return bookDAO.findById(bid).orElse(null);
    }

    public List<Book> listByCategory(int cid) {
        BookCategory category = categoryService.get(cid);
        return bookDAO.findAllByBookCategory(category);
    }

    public List<Book> listByUser(int uid) {
        User user = userService.get(uid);
        return bookDAO.findAllByUser(user);
    }

}
