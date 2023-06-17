package com.winterfoodies.winterfoodies_project.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;


@Getter
public class ResponseBox extends HashMap<String,Object> {

    @JsonIgnore
    private HttpStatus status;

   public ResponseBox message(String message){
       this.put("message", message);
       return this;
   }
   public ResponseBox redirect(String redirect){
       this.put("redirect", redirect);
       return this;
   }

   public void status(HttpStatus status){
       this.status = status;
   }
}
