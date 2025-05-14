package com.wbtracker.pricebot.productFacade;

import com.wbtracker.pricebot.dto.ProductDto;
import com.wbtracker.pricebot.entity.ProductEntity;
import com.wbtracker.pricebot.mapper.ProductEntityMapper;
import com.wbtracker.pricebot.parser.WildberriesProductParser;
import com.wbtracker.pricebot.repository.WbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final WildberriesProductParser parser;
    private final ProductEntityMapper mapper;
    private final WbRepository repository;

    public ProductDto parseAndSave(String url) {
        // Парсим DTO
        ProductDto dto = parser.parseFromLink(url);

        // Маппим в сущность
        ProductEntity entity = mapper.toEntity(dto);

        // Сохраняем
        ProductEntity saved = repository.save(entity);

        // Возвращаем обратно DTO (можно вернуть saved → dto)
        return mapper.toDto(saved);
    }
}
