package com.pandyzer.backend.models.dto;

public class QuantityDTO {

    private Integer quantity;

    public QuantityDTO () {}

    public QuantityDTO (Integer quantity) {

        this.quantity = quantity;

    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
