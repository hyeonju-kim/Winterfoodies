package com.winterfoodies.winterfoodies_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Review extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;

    private String storeName;
    private Long rating;
    private Long userId;
    private byte[] photo;
    private String content;
//    private LocalDateTime timestamp;  -> 이건 추가할 필요 없음!!!!!!  reviewDto에는 시작시간 담아야하니까 추가해준것

}