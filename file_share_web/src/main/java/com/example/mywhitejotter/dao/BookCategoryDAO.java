package com.example.mywhitejotter.dao;

import com.example.mywhitejotter.pojo.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryDAO extends JpaRepository<BookCategory, Integer> {

}
