package com.winterfoodies.winterfoodies_project.entity;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "QUANTITY")
    private Long quantity;

    @Column(name = "CLIENT_MESSAGE")
    private String clientMessage;

    @Column(name = "VISIT_TIME")
    private LocalDateTime visitTime;

    @Column(name = "SUB_TOTAL_AMOUNT")
    private Long subTotalAmount;

    public OrderProduct() {

    }
    public OrderProduct(Product product) {
        this.product = product;
    }
}
