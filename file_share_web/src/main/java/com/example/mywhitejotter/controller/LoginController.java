package com.example.mywhitejotter.controller;

import com.example.mywhitejotter.bean.Result;
import com.example.mywhitejotter.pojo.User;
import com.example.mywhitejotter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value="api/login")
    @ResponseBody
    public Result log(@RequestBody User requestUser, HttpSession session) {
        String TAG = "LoginController";

        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username); // 将类似 <div>hello world</div> 转换为：&lt;div&gt;hello world&lt;/div&gt;

        User user = userService.get(username, requestUser.getPassword());
        if (user != null) {
            session.setAttribute("user", user);
            System.out.println(TAG +  " -> login successful; set user = " + user);

            String uid = String.valueOf(user.getId());
            return new Result(200, uid);
        } else {
            return new Result(400, "密码错误或者用户不存在!");
        }
    }

}
