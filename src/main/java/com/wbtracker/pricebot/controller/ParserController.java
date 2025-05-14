package com.wbtracker.pricebot.controller;

import com.wbtracker.pricebot.dto.ProductDto;
import com.wbtracker.pricebot.productFacade.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ParserController {

    private final ProductFacade productFacade;

    @GetMapping("/parse")
    public ProductDto save(@RequestParam String url) {
        return productFacade.parseAndSave(url);
    }
}