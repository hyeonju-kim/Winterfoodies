package com.winterfoodies.winterfoodies_project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) //시퀀스 사용가능한 DB에서 사용. H2, 오라클에서 사용가능. H2에서 테스트하니까 이렇게 쓰자. 개발할땐 이렇게 하고 추후에 identity로 바꾼다.
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USER_NAME")
    private String name;

    @Column(name = "USER_PASSWORD")
    private String password;

    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String email;

}
