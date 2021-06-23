package com.gafur.packtask.service;

import com.gafur.packtask.entity.BackpackSample;
import com.gafur.packtask.entity.BackpackSolution;
import com.gafur.packtask.entity.Item;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import org.springframework.stereotype.Service;

@Service
public class PackByMaxCostService implements PackService {
    private static final int MAX_ITEM_COUNT = 15;

    private double[][] memoryTable;

    @Override
    public BackpackSolution packItemsByMaxCost(BackpackSample backPack) {
        int maxWeight = backPack.getMaxWeight();
        List<Item> items = backPack.getItems();
        Collections.sort(items, Comparator.comparing(Item::getWeight));

        memoryTable = new double[maxWeight + 1][items.size()];

        for (int j = 0; j < maxWeight + 1; j++) {
            for (int i = 0; i < items.size(); i++) {
                memoryTable[j][i] = -1;
            }
        }
        getCell(maxWeight, items.size() - 1, items);

        PriorityQueue<Item> heap = new PriorityQueue<>(Comparator.comparingDouble(Item::getCost).reversed());
        BackpackSolution best = traceTable(maxWeight, items, heap);
        getByMaxItemLimit(best, heap);
        Collections.sort(best.getItems(), Comparator.comparing(Item::getId));
        return best;
    }

    // Traces back table
    private BackpackSolution traceTable(int maxWeight, List<Item> items, PriorityQueue<Item> heap) {
        BackpackSolution best = new BackpackSolution();

        int i = items.size() - 1;
        int j = maxWeight;

        while (i >= 0) {
            Item item = items.get(i);
            double without = i == 0 ? 0 : memoryTable[j][i - 1];

            if (memoryTable[j][i] != without) {
                heap.add(item);
                j -= (int) item.getWeight();
            }
            i--;
        }
        return best;
    }

    // Uses recursion with memoization
    private double getCell(int j, int i, List<Item> items) {
        if (i < 0 || j < 0) {
            return 0;
        }
        Item item = items.get(i);

        double with;
        double without;
        double cell = memoryTable[j][i];

        // If not memoized
        if (cell == -1) {
            if (item.getWeight() > j) {
                with = -1;
            } else {
                with = item.getCost() + getCell(j - (int) item.getWeight(), i - 1, items);
            }
            without = getCell(j, i - 1, items);
            cell = Math.max(with, without);
            // Memoize
            memoryTable[j][i] = cell;
        }
        return cell;
    }


    private void getByMaxItemLimit(BackpackSolution solution, PriorityQueue<Item> heap) {
        List<Item> items = solution.getItems();
        for (int i = 0; !heap.isEmpty() && i < MAX_ITEM_COUNT; i++) {
            items.add(heap.poll());
        }
    }
}
