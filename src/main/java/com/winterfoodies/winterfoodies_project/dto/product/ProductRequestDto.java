package com.winterfoodies.winterfoodies_project.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ProductRequestDto {
    @NotNull @Min(0)
    private Long id;

    private Long quantity;
    private String clientMessage;
}
