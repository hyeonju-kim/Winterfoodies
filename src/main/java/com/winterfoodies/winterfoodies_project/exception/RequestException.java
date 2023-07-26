package com.winterfoodies.winterfoodies_project.exception;

import com.winterfoodies.winterfoodies_project.ErrorBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestException extends RuntimeException {
    private ErrorBox errorBox;
    public RequestException(ErrorBox errorBox) {
        this.errorBox = errorBox;
    }

    public RequestException() {

    }
}
