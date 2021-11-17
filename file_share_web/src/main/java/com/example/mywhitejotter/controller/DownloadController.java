package com.example.mywhitejotter.controller;

import com.example.mywhitejotter.bean.UserInfo;
import com.example.mywhitejotter.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DownloadController {
    @Autowired
    DownloadService downloadService;

    /**
     * 返回下载同一书本的用户类表
     * @param bid
     * @return
     */
    @CrossOrigin
    @GetMapping("/api/book/{bid}/users")
    public List<UserInfo> listByBook(@PathVariable("bid") int bid) {
        return downloadService.listByBook(bid);
    }
}
