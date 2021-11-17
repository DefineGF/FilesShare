package com.example.mywhitejotter.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * id
 * 种类id & 上传者id & 封面 &书名 & 作者 & 上传时间 & 简介
 */
@Entity
@Table(name = "book")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name="cid")
    private BookCategory bookCategory;

    @ManyToOne
    @JoinColumn(name="uid")
    private User user;

    private String cover;
    private String name;
    private String author;

    private String uploadDate;
    private String brief;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}
