package com.kuit.moamoa.dto;

import com.kuit.moamoa.domain.Item;
import lombok.Getter;

@Getter
public class ItemResponse {
    Long itemId;
    String imageUrl;
    int price;

    public ItemResponse(Item item) {
        this.itemId = item.getId();
        this.imageUrl = item.getImageUrl();
        this.price = Math.toIntExact(item.getPrice());
    }
}
