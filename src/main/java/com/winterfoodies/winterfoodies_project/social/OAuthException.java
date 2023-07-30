package com.winterfoodies.winterfoodies_project.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OAuthException extends RuntimeException {

    private final String message;
}
