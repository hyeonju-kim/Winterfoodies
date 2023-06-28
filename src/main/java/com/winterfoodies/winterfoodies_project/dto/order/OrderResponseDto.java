package com.winterfoodies.winterfoodies_project.dto.order;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.OrderProduct;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class OrderResponseDto {
    private Long storeId;
    private String storeName;
    private Long orderId;
    private Long customerId;
    private String customerName;
    private LocalDateTime orderDate;
    private String processYn;
    private String customerMessage;
    private List<Map<String, Long>> productAndQuantityList;
    private Long totalAmount;
}
