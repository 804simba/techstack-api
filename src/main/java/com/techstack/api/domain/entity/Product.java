package com.techstack.api.domain.entity;

import com.techstack.api.domain.entity.base.BaseEntity;
import com.techstack.api.payload.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
public class Product extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "minimum_opening_balance")
    private BigDecimal minimumOpeningBalance;

    @Column(name = "maximum_opening_balance")
    private BigDecimal maximumOpeningBalance;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType type;
}
