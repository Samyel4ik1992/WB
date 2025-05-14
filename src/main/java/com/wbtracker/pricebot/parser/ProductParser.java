package com.wbtracker.pricebot.parser;

import com.wbtracker.pricebot.dto.ProductDto;

interface ProductParser {
    ProductDto parseFromLink(String url);
}
