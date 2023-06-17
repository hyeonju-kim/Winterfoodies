package com.winterfoodies.winterfoodies_project.dto.order;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {
    private Long storeId;
    private Long orderId;
    private Long customerId;
    private String customerName;
    private String orderDate;
    private String processYn;
    private String customerMessage;
    private String orderMenu;
}
