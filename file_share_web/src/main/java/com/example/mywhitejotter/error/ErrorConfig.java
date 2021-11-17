package com.example.mywhitejotter.error;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorConfig implements ErrorPageRegistrar {
    private static final String TAG = "ErrorConfig";
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        System.out.println(TAG + "register_error_pages");
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
        registry.addErrorPages(errorPage404);
    }
}
