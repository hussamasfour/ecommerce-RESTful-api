package com.hussam.inventory.inventory.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


public class CartRequest {

    @Min(value = 1,message = "Please enter a valid product id")
    private Long productId;

    @Size(min = 1, max = 99, message = "Quantity of product should be between 1-99")
    private int quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
