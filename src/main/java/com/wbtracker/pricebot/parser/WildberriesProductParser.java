package com.wbtracker.pricebot.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbtracker.pricebot.dto.ProductDto;
import com.wbtracker.pricebot.enums.CurrencyCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class WildberriesProductParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ProductDto parseFromLink(String url) {
        try {
            Long nmId = extractNmIdFromUrl(url);

            String apiUrl = "https://card.wb.ru/cards/v1/detail?appType=1&curr=rub&dest=-1257786&nm=" + nmId;

            Document jsonDoc = Jsoup.connect(apiUrl)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0")
                    .get();

            String json = jsonDoc.body().text();
            JsonNode root = objectMapper.readTree(json);
            JsonNode product = root.at("/data/products/0");

            if (product.isMissingNode()) {
                throw new IllegalArgumentException("Продукт не найден в JSON по ссылке: " + url);
            }

            String name = product.get("name").asText();
            int price = product.get("priceU").asInt() / 100;
            int salePrice = product.get("salePriceU").asInt() / 100;
            double rating = product.path("reviewRating").asDouble(0.0);

            return ProductDto.builder()
                    .nmId(nmId)
                    .name(name)
                    .price(price)
                    .salePrice(salePrice)
                    .rating(rating)
                    .currency(CurrencyCode.RUB)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при получении данных с WB JSON API", e);
        }
    }

    private Long extractNmIdFromUrl(String url) {
        Pattern pattern = Pattern.compile("/catalog/(\\d+)/");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new IllegalArgumentException("Не удалось извлечь nmId из URL: " + url);
    }
}
