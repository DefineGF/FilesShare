package com.example.mywhitejotter.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="download")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Download {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    int id;

    @ManyToOne
    @JoinColumn(name="bid")
    private Book book;

    @ManyToOne
    @JoinColumn(name="uid")
    private User user;

    private String downloadDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }
}
