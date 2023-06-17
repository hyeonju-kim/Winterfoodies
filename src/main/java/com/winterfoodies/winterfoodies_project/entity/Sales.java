package com.winterfoodies.winterfoodies_project.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SALES")
@Setter
@Getter
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SALES_ID")
    private Long id;
    private Long customerId;
    private Long totalPrice;
    private Long productId;
    private LocalDateTime orderAt;
}
