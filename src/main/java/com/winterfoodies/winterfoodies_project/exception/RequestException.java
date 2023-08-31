package com.winterfoodies.winterfoodies_project.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.ErrorBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RequestException extends RuntimeException {
    private ErrorBox errorBox;
    public RequestException(ErrorBox errorBox) {
        this.errorBox = errorBox;
    }

    public RequestException() {

    }
}
