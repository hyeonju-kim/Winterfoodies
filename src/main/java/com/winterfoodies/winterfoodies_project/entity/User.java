package com.winterfoodies.winterfoodies_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winterfoodies.winterfoodies_project.dto.user.UserRequestDto;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.usertype.UserType;
//import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS", indexes = {
        @Index(name = "USER_IDX", columnList = "USER_EMAIL", unique = true)
})
@Getter
@Setter
@SequenceGenerator(name = "userSeq", sequenceName = "USER_SEQ", initialValue = 1, allocationSize = 1)
@NoArgsConstructor
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

    private double latitude;
    private double longitude;

    @JsonIgnore // 재귀순환 방지
    @OneToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Order> order = new ArrayList<>();

    public User(UserRequestDto userRequestDto, UserType type){
        this.username = userRequestDto.getUsername();
        this.email = userRequestDto.getEmail();
        this.phoneNumber = userRequestDto.getPhoneNumber();
        this.password = userRequestDto.getPassword();
    }

//    public void encryptPassword(PasswordEncoder passwordEncoder){
//        this.password = passwordEncoder.encode(this.password);
//    }

}
