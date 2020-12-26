package com.hussam.inventory.inventory.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class IncreaseCartElementRequest {
    @NotBlank
    @Min(1)
    private Long elementId;
    @NotBlank
    @Min(1)
    private int amount;

    public Long getElementId() {
        return elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
