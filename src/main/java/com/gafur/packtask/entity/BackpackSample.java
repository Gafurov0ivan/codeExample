package com.gafur.packtask.entity;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Value;

@Value
public class BackpackSample {
    @Min(0)
    @Max(100)
    private final int maxWeight;
    private final List<Item> items;

    public BackpackSample(int maxWeight, List<Item> items) {
        this.maxWeight = maxWeight;
        this.items = items;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public List<Item> getItems() {
        return items;
    }
}
