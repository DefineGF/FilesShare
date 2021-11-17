package com.example.mywhitejotter.dao;

import com.example.mywhitejotter.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data Access Object（数据访问对象 UAO）用来操作数据库的对象
 * 这里通过继承 JpaRepository
 */
public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(String name);
    User getByUsernameAndPassword(String name, String psw);
}
