package com.kuit.moamoa.dto;

import com.kuit.moamoa.domain.Item;
import com.kuit.moamoa.domain.PurchaseRecord;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class AdornProfileResponse {
    List<ItemResponse> items;
    List<Long> boughtItemId;

    public AdornProfileResponse(List<Item> items, List<PurchaseRecord> purchaseRecords) {
        this.items = items.stream()
                .map(ItemResponse::new)
                .toList();
        this.boughtItemId = purchaseRecords.stream()
                .map(PurchaseRecord::getItemId)
                .collect(Collectors.toList());
    }
}
