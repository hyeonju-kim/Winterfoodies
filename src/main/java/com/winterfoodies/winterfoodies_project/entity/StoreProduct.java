package com.winterfoodies.winterfoodies_project.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "STORE_PRODUCT")
@Getter
@Setter
@SequenceGenerator(name = "storeProductSeq", sequenceName = "STORE_PRODUCT_SEQ", initialValue = 1, allocationSize = 1)
public class StoreProduct {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storeProductSeq")
    @Column(name = "STORE_PRODUCT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "PRODUCT_STATUS_FLAG")
    private String productStatusFlag;
}
