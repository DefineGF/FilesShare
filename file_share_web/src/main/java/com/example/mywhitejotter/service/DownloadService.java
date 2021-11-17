package com.example.mywhitejotter.service;

import com.example.mywhitejotter.bean.UserInfo;
import com.example.mywhitejotter.dao.DownloadDAO;
import com.example.mywhitejotter.pojo.Book;
import com.example.mywhitejotter.pojo.Download;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DownloadService {
    @Autowired
    DownloadDAO downloadDAO;

    @Autowired
    BookService bookService;

    public void addOrUpdate(Download download) {
        downloadDAO.save(download);
    }

    public List<UserInfo> listByBook(int bid) {
        Book book = bookService.get(bid);
        List<Download> downloads = downloadDAO.findAllByBook(book);
        List<UserInfo> users = new LinkedList<>();
        for (Download download : downloads) {
            users.add(new UserInfo(download.getUser().getId(), download.getUser().getUsername()));
        }
        return users;
    }
}
