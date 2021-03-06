package com.example.mywhitejotter.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
/**
 *  @JsonIgnoreProperties({ “handler”,“hibernateLazyInitializer” }):
 *      因为是做前后端分离，而前后端数据交互用的是 json 格式。
 *      User 对象就会被转换为 json 数据;
 *      使用 jpa 来做实体类的持久化，jpa 默认会使用 hibernate, 在 jpa 工作过程中，就会创造代理类来继承 User ;
 *      并添加 handler 和 hibernateLazyInitializer 这两个无须 json 化的属性，
 *      所以这里需要用 JsonIgnoreProperties 把这两个属性忽略掉
 */

@Entity                     // 表示 User 是个实体
@Table(name = "user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String username;
    String password;

    private int points;
    private int permission;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", points=" + points +
                ", permission=" + permission +
                '}';
    }
}

