package com.example.mywhitejotter.service;

import com.example.mywhitejotter.dao.UserDAO;
import com.example.mywhitejotter.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public boolean isExist(String name) {
        User user = getByName(name);
        return user != null;
    }

    public User getByName(String name) {
        return userDAO.findByUsername(name);
    }

    public User get(int uid) {
        return userDAO.findById(uid).orElse(null);
    }

    public User get(String name, String psw) {
        return userDAO.getByUsernameAndPassword(name, psw);
    }

    public void add(User user) {
        userDAO.save(user);
    }
}
