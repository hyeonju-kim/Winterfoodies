package com.winterfoodies.winterfoodies_project.dto.order;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.dto.product.ProductDto;
import com.winterfoodies.winterfoodies_project.entity.OrderProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class OrderResponseDto {

    @ApiModelProperty(example = "1", value = "주문 id", hidden = true)
    private double storeId;

    @ApiModelProperty(example = "1", value = "주문 id")
    private String storeName;

    @ApiModelProperty(example = "1", value = "주문 id")
    private double orderId;

    @ApiModelProperty(example = "1", value = "주문 id", hidden = true)
    private double customerId;

    @ApiModelProperty(example = "1", value = "주문 id", hidden = true)
    private String productId;

    @ApiModelProperty(example = "1", value = "주문 id", hidden = true)
    private String productName;

    @ApiModelProperty(example = "1", value = "주문 id", hidden = true)
    private String customerName;

    @ApiModelProperty(value = "주문 일자")
    private LocalDateTime orderDate;

    @ApiModelProperty(example = "1", value = "주문 id", hidden = true)
    private String processYn;

    @ApiModelProperty(example = "빨리 만들어 주세용~~!", value = "사용자 메시지")
    private String customerMessage;

    @ApiModelProperty(value = "상품 및 수량 리스트")
    private List<ProductDto> productAndQuantityList;

    @ApiModelProperty(example = "10000", value = "총 주문금액")
    private double totalAmount;

    @ApiModelProperty(example = "3000", value = "각 메뉴별 총 주문금액")
    private double subTotalAmount;

    @ApiModelProperty(example = "20분~30분" , value = "조리예상시간")
    private String estimatedCookingTime;

    private double averageRating;
}
