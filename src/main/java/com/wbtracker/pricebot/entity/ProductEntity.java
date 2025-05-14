package com.wbtracker.pricebot.entity;

import com.wbtracker.pricebot.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Внутренний ID в БД

    @Column(name = "nm_id", unique = true, nullable = false)
    private Long nmId; // Идентификатор товара на WB

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price; // Без скидки

    @Column(name = "sale_price", nullable = false)
    private int salePrice; // Со скидкой

    @Column(name = "rating", nullable = false)
    private double rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 10)
    private CurrencyCode currency;
}
