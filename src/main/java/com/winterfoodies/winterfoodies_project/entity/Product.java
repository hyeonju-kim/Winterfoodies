package com.winterfoodies.winterfoodies_project.entity;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCT")
@SequenceGenerator(name = "productSeq", sequenceName = "product_seq", allocationSize = 1, initialValue = 1)
@Getter
@Setter
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productSeq")
    @Column(name = "PRODUCT_ID")
    private Long id;

    @Column(name = "PRODUCT_NAME")
    private String name;

    @Column(name = "PRICE")
    private Long price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    List<StoreProduct> storeProducts = new ArrayList<>();

}
