package com.winterfoodies.winterfoodies_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "STORE_PRODUCT")
@Getter
@Setter
@SequenceGenerator(name = "storeProductSeq", sequenceName = "STORE_PRODUCT_SEQ", initialValue = 1, allocationSize = 1)
public class StoreProduct {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storeProductSeq")
    @Column(name = "STORE_PRODUCT_ID")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "PRODUCT_STATUS_FLAG")
    private String productStatusFlag;
}
