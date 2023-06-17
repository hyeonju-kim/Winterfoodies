package com.winterfoodies.winterfoodies_project.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ORDER_PRODUCT")
@Getter
@Setter
@SequenceGenerator(name = "orderProductSeq", sequenceName = "ORDER_PRODUCT_SEQ", initialValue = 1, allocationSize = 1)
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderProductSeq")
    @Column(name = "ORDER_PRODUCT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "QUANTITY")
    private Long quantity;

    @Column(name = "CLIENT_MESSAGE")
    private String clientMessage;

    @Column(name = "VISIT_TIME")
    private LocalDateTime visitTime;

}
