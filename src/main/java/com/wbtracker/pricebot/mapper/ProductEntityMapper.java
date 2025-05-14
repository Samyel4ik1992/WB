package com.wbtracker.pricebot.mapper;

import com.wbtracker.pricebot.dto.ProductDto;
import com.wbtracker.pricebot.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ProductEntityMapper {
    ProductEntity toEntity(ProductDto productDto1);

    ProductDto toDto(ProductEntity productEntity);

}