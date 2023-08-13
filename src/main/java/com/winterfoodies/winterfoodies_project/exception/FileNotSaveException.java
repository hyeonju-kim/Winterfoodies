package com.winterfoodies.winterfoodies_project.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileNotSaveException extends RuntimeException{
    private String message;
}
