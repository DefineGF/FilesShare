package com.example.mywhitejotter.dao;

import com.example.mywhitejotter.pojo.Book;
import com.example.mywhitejotter.pojo.Download;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DownloadDAO extends JpaRepository<Download, Integer> {
    List<Download> findAllByBook(Book book);
}

