package com.wbtracker.pricebot.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbtracker.pricebot.dto.ProductDto;
import com.wbtracker.pricebot.enums.CurrencyCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class WildberriesProductParser implements ProductParser {

    private static final Pattern WB_JSON_PATTERN =
            Pattern.compile("window\\.__WB_DATA__\\s*=\\s*(\\{.*?\\});", Pattern.DOTALL);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ProductDto parseFromLink(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Matcher matcher = WB_JSON_PATTERN.matcher(doc.html());
            if (!matcher.find()) {
                log.warn("Не найден JSON-блок на странице: {}", url);
                throw new RuntimeException("WB JSON not found");
            }

            String json = matcher.group(1);
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode productNode = rootNode.path("product");

            if (productNode.isMissingNode() || productNode.isEmpty()) {
                log.warn("Не удалось найти 'product' в JSON для URL: {}", url);
                throw new RuntimeException("Invalid product JSON structure");
            }

            CurrencyCode currency = extractCurrencyFromUrl(url);

            ProductDto dto = new ProductDto();
            dto.setNmId(productNode.path("id").asLong());
            dto.setName(productNode.path("name").asText(""));
            dto.setPrice(productNode.path("priceU").asInt(0) / 100);
            dto.setSalePrice(productNode.path("salePriceU").asInt(0) / 100);
            dto.setRating(productNode.path("reviewRating").asDouble(0.0));
            dto.setCurrency(currency);

            log.info("Успешно спарсен товар: {} ({}): {} -> {} {}",
                    dto.getNmId(), dto.getName(), dto.getPrice(), dto.getSalePrice(), currency);

            return dto;

        } catch (Exception e) {
            log.error("Ошибка при парсинге WB-страницы: {}", url, e);
            throw new RuntimeException("Ошибка при обработке Wildberries-ссылки", e);
        }
    }

    private CurrencyCode extractCurrencyFromUrl(String url) {
        if (url.contains(".ru")) return CurrencyCode.RUB;
        if (url.contains(".by")) return CurrencyCode.BYN;
        if (url.contains(".kz")) return CurrencyCode.KZT;
        if (url.contains(".uz")) return CurrencyCode.UZS;
        if (url.contains(".am")) return CurrencyCode.AMD;
        return CurrencyCode.UNKNOWN;
    }
}
