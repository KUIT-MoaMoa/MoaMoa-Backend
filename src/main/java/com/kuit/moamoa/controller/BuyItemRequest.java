package com.kuit.moamoa.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class BuyItemRequest {
    private Long itemId;
    private int price;
}
