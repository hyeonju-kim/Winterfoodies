package com.winterfoodies.winterfoodies_project.entity;

import com.winterfoodies.winterfoodies_project.dto.store.StoreRequestDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "STORE")
@Getter
@Setter
@SequenceGenerator(name = "storeSeq", sequenceName = "STORE_SEQ", initialValue = 1, allocationSize = 1)
public class Store implements Serializable {

    static final long serialVersionUID = -3085157956097560248L;
    @Column(name = "STORE_ID")
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storeSeq")
    private Long id;

    @Column(name = "STORE_STATUS")
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @OneToOne(mappedBy = "store")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "STORE_DETAIL_ID")
    private StoreDetail storeDetail;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
    private List<StoreProduct> storeProducts = new ArrayList<>();

    public void changeUser(User user){
        this.user = user;
    }
    public void changeStoreDetail(StoreDetail storeDetail){
        this.storeDetail = storeDetail;
    }
    public void changeStatus(StoreRequestDto storeRequestDto){
        this.status = storeRequestDto.getStatus();
    }

}
