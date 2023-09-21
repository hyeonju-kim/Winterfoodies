package com.winterfoodies.winterfoodies_project.dto.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ProductRequestDto {
    @NotNull
    @Min(0)
    @ApiModelProperty(example = "2", value = "상품 아이디")
    private double productId;

    @NotNull
    @Min(0)
    @ApiModelProperty(example = "5", value = "상품 수량")
    private double quantity;

    @ApiModelProperty(example = "유저 메시지" , hidden = true)
    private String clientMessage;

    @ApiModelProperty(example = "1", value = "가게 id")
    private double storeId;
}
