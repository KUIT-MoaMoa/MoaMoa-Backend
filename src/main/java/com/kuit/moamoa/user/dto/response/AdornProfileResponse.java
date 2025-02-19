package com.kuit.moamoa.user.dto.response;

import com.kuit.moamoa.user.domain.Item;
import com.kuit.moamoa.user.domain.PurchaseRecord;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class AdornProfileResponse {
    String boarderUrl;
    List<ItemResponse> items;
    List<Long> boughtItemId;

    public AdornProfileResponse(String boarderUrl, List<Item> items, List<PurchaseRecord> purchaseRecords) {
        this.boarderUrl = boarderUrl;
        this.items = items.stream()
                .map(ItemResponse::new)
                .toList();
        this.boughtItemId = purchaseRecords.stream()
                .map(PurchaseRecord::getItemId)
                .collect(Collectors.toList());
    }
}
