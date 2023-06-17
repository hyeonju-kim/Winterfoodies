package com.winterfoodies.winterfoodies_project.entity;


import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@SequenceGenerator(name = "orderSeq", sequenceName = "order_seq", allocationSize = 1, initialValue = 1)
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeq")
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @Column(name = "PROCESS_YN")
    private String processYn;

    @Column(name = "CREATED_AT")
    private LocalDateTime createAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderProduct> orderProducts = new ArrayList<>();

}
