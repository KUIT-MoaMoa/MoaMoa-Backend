package com.kuit.moamoa.user.dto.response;

import com.kuit.moamoa.user.domain.Item;
import com.kuit.moamoa.user.domain.PurchaseRecord;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class AdornProfileResponse {
    int currentBoarderId;
    List<ItemResponse> items;
    List<Long> boughtItemId;

    public AdornProfileResponse(String boarderUrl, List<Item> items, List<PurchaseRecord> purchaseRecords)
            throws MalformedURLException {
        URL url = new URL(boarderUrl);

        // Get the path portion of the URL: "/boarder/1.svg"
        String path = url.getPath();

        // Extract the file name by getting the substring after the last '/'
        String fileName = path.substring(path.lastIndexOf('/') + 1); // "1.svg"

        // Remove the ".svg" extension to get the numeric part
        String numberStr = fileName.replaceAll("\\.svg$", "");

        // Parse the string to an integer (if needed)
        int number = Integer.parseInt(numberStr);
        this.currentBoarderId = number;
        this.items = items.stream()
                .map(ItemResponse::new)
                .toList();
        this.boughtItemId = purchaseRecords.stream()
                .map(PurchaseRecord::getItemId)
                .collect(Collectors.toList());
        boughtItemId.add(1L);
    }
}
