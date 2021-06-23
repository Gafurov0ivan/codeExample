package com.gafur.packtask.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class BackpackSolution {
    private final List<Item> items;

    public BackpackSolution() {
        this.items = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            builder.append(items.get(i).getId());
            if (i < items.size() - 1) {
                builder.append(",");
            }
        }
        if (items.size() == 0) {
            builder.append("-");
        }

        return builder.toString();
    }
}
