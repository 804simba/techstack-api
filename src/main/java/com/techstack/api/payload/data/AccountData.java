package com.techstack.api.payload.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountData {
    @JsonProperty("account_id")
    private Long id;
    private String name;

    private String type;

    @JsonProperty("account_number")
    private String accountNumber;

    private BigDecimal balance;

    private String status;

    private ProductData product;
}
