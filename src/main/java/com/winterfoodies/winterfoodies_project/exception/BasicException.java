package com.winterfoodies.winterfoodies_project.exception;

public class BasicException extends RuntimeException{
    private String redirect;

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
