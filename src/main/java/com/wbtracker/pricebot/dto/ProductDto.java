package com.wbtracker.pricebot.dto;

import com.wbtracker.pricebot.entity.ProductEntity;
import com.wbtracker.pricebot.enums.CurrencyCode;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO for {@link ProductEntity}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @NotNull
    private Long nmId;
    @Size(max = 255)
    @NotBlank
    private String name;
    @Positive
    private int price;
    @PositiveOrZero
    private int salePrice;
    @Min(0)
    @Max(5)
    private double rating;
    @NotNull
    private CurrencyCode currency;
}