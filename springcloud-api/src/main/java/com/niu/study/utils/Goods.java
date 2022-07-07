package com.niu.study.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试商品属性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    public String img;
    public String goodsPrice;
    public String title;

}
