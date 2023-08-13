package com.winterfoodies.winterfoodies_project.exception;

import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreException extends BasicException{
    private String message;
    private HttpStatus status;
    private StoreResponseDto userResponseDto;
}
