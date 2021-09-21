package com.poplar.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * BY Alex CREATED 2021/9/21
 * 奖品配置
 */
@Data
@AllArgsConstructor
public class PrizeConfig {

    private Integer id;

    private String prizeName;

    private BigDecimal amount;

    private double probability;

    private int quantity;
}
