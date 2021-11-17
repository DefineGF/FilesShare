package com.example.mywhitejotter.service;

import com.example.mywhitejotter.dao.BookCategoryDAO;
import com.example.mywhitejotter.pojo.BookCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCategoryService {
    @Autowired
    BookCategoryDAO categoryDAO;

    public List<BookCategory> list() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    public BookCategory get(int id) {
        return categoryDAO.findById(id).orElse(null);
    }
}
