package com.wbtracker.pricebot.productFacade;

import com.wbtracker.pricebot.dto.ProductDto;
import com.wbtracker.pricebot.parser.WildberriesProductParser;
import com.wbtracker.pricebot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final WildberriesProductParser parser;
    private final ProductService productService;

    public ProductDto parseAndSave(String url) {
        ProductDto dto = parser.parseFromLink(url);
        return productService.saveIfChanged(dto); // ✅ новая логика
    }
}
