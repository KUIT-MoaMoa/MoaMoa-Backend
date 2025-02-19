package com.kuit.moamoa.user.dto.response;

import com.kuit.moamoa.user.domain.Item;
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
