package com.techstack.api.payload.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductData {
    @JsonProperty("product_id")
    private Long id;
    private String name;
    private String description;
    private BigDecimal minimumOpeningBalance;
    private BigDecimal maximumOpeningBalance;
    private BigDecimal interestRate;
    private String currency;
    private String type;
}