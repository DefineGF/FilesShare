package com.example.mywhitejotter.interceptor;

import com.example.mywhitejotter.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 需要将其配置 WebConfig 中
 */
public class LoginInterceptor implements HandlerInterceptor {
    private static final String TAG = "LoginInterceptor";

    // 定义以 index 开头的路径都会被转发，包括 index.html
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String[] requireAuthPages = new String[] {  // 需要拦截的路径: 实际上以 index开头的路径都会拦截 （含index 和 index.html)
                "library","individ"
        };
        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();

        String uri = request.getRequestURI();

        System.out.println(TAG + " >> contextPath = " + contextPath + " ;uri = " + uri);

        String page = StringUtils.remove(uri, contextPath + "/");
        if (beginningWith(page, requireAuthPages)) {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("login");
                return false;
            }
        }
        System.out.println(TAG + " -> 不做拦截！！！");
        return true;
    }

    private boolean beginningWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String requiredPage : requiredAuthPages) {
            if (StringUtils.startsWith(page, requiredPage)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
