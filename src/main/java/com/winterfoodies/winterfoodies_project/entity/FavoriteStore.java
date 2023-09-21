package com.winterfoodies.winterfoodies_project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.GenerationType;

@Entity
@Getter
@Setter
@Table(name = "FAVORITE_STORE")
public class FavoriteStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "STORE_ID")
    private Long storeId;

}
