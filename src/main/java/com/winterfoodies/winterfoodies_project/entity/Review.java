package com.winterfoodies.winterfoodies_project.entity;

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

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;

    private String storeName;
    private Long rating;
    private Long userId;
    private byte[] photo;
    private String content;
//    private LocalDateTime timestamp;

}
