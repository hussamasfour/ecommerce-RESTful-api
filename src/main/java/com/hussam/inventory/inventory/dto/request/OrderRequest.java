package com.hussam.inventory.inventory.dto.request;

import javax.validation.constraints.Min;


public class OrderRequest {

    @Min(value = 1, message = "quantity can't be less than 1")

    private int productQuantity;

    @Min(value = 1, message = "Please enter a valid product id")
    private Long productId;
//
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}

