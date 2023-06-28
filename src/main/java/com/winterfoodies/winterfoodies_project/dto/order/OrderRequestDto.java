package com.winterfoodies.winterfoodies_project.dto.order;

import com.winterfoodies.winterfoodies_project.dto.product.ProductRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderRequestDto {
    private Long orderId;
    private List<ProductRequestDto> productRequestDtoList;
    private Long storeId;
    private String message;
}
