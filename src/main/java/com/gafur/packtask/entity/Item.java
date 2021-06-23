package com.gafur.packtask.entity;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import lombok.Value;

@Value
public class Item {
    @Min(1)
    private final long id;
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private final double weight;
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private final double cost;

    public Item(long id, double weight, double cost) {
        this.id = id;
        this.weight = weight;
        this.cost = cost;
    }
}
