package com.winterfoodies.winterfoodies_project.entity;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@SequenceGenerator(name = "orderSeq", sequenceName = "order_seq", allocationSize = 1, initialValue = 1)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
//@JsonIgnoreProperties("user")  // 순환참조 오류나서 잠시 넣음!!
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeq")
    @Column(name = "ORDER_ID")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @Column(name = "PROCESS_YN")
    private String processYn;

    @Column(name = "CREATED_AT")
    private LocalDateTime createAt;

    @Column(name = "TOTAL_AMOUNT")
    private Long totalAmount;

    @Column(name = "MESSAGE")
    private String message;

    @OneToOne
    private Review review;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderProduct> orderProducts = new ArrayList<>();

}
