package com.hussam.inventory.inventory.dto.request;

public class DecrementCartElementRequest {

    private Long elementId;

    private Integer amount;

    public Long getElementId() {
        return elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
