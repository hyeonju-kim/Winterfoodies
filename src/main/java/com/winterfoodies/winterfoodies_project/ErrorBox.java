package com.winterfoodies.winterfoodies_project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorBox {
    private String cause;
    private String message;

    public ErrorBox() {

    }

    public ErrorBox(String cause) {
        this.cause = cause;
    }


    public ErrorBox(String cause, String message) {
        this.cause = cause;
        this.message = message;
    }
}
