package com.winterfoodies.winterfoodies_project.dto.order;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.OrderProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class OrderResponseDto {
    private Long storeId;
    private String storeName;
    private Long orderId;
    private Long customerId;
    private String customerName;
    private String orderDate;
    private String processYn;
    private String customerMessage;
    private List<String> orderMenu; // List<OrderProduct> 라고 하니까 순환참조 오류가 났다!!
    private Long totalAmount;
}
