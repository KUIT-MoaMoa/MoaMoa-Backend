package com.kuit.moamoa.controller;

import lombok.Getter;

@Getter
public class BuyItemRequest {
    Long itemId;
    String itemName;
    int price;
}
