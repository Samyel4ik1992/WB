package com.wbtracker.pricebot.repository;

import com.wbtracker.pricebot.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WbRepository extends JpaRepository<ProductEntity,Long> {
}
