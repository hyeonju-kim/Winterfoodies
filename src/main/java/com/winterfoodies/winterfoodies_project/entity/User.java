package com.winterfoodies.winterfoodies_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.winterfoodies.winterfoodies_project.dto.user.UserRequestDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USERS", indexes = {
        @Index(name = "USER_IDX", columnList = "USER_EMAIL", unique = true)
})
@Getter
@Setter
@SequenceGenerator(name = "userSeq", sequenceName = "USER_SEQ", initialValue = 1, allocationSize = 1)
//@JsonIgnoreProperties("order")  // 순환참조 오류나서 잠시 넣음!!
public class User implements Serializable {
    static final long serialVersionUID = -3085157956097560247L;

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "USER_PASSWORD")
    private String password;

    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "USER_PHONE_NUMBER")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_STATUS")
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE")
    private UserType role;
//
//    @Column(name = "LATITUDE")
//    private double latitude;
//
//    @Column(name = "LONGITUDE")
//    private double longitude;

    @JsonIgnore // 재귀순환 방지
    @OneToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @OneToMany(mappedBy = "user")
    private List<Order> order = new ArrayList<>();

    public Collection<Order> getOrder() {
        return order;
    }

//    public void setOrder(Collection<Order> order) {
//        this.order = order;
//    }

    public User(UserRequestDto userRequestDto, UserType type){
        this.username = userRequestDto.getUsername();
        this.email = userRequestDto.getEmail();
        this.phoneNumber = userRequestDto.getPhoneNumber();
        this.password = userRequestDto.getPassword();
//        this.latitude = userRequestDto.getLatitude();
//        this.longitude = userRequestDto.getLongitude();
    }

    public User(UserRequestDto userRequestDto) {
        this.username = userRequestDto.getUsername();
        this.email = userRequestDto.getEmail();
        this.phoneNumber = userRequestDto.getPhoneNumber();
        this.password = userRequestDto.getPassword();
//        this.latitude = userRequestDto.getLatitude();
//        this.longitude = userRequestDto.getLongitude();
    }

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }


}
