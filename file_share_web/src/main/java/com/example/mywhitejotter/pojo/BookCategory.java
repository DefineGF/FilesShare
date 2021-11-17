package com.example.mywhitejotter.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="book_category")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class BookCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    int id;

    String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
