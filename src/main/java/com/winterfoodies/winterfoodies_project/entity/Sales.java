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
    private double id;
    private double customerId;
    private double totalPrice;
    private double productId;
    private LocalDateTime orderAt;
}
