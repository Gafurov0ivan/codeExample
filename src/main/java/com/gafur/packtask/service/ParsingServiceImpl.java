package com.gafur.packtask.service;

import com.gafur.packtask.entity.BackpackSample;
import com.gafur.packtask.entity.InputIllegalArgumentException;
import com.gafur.packtask.entity.Item;
import com.gafur.packtask.util.FileUtil;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ParsingServiceImpl implements ParsingService {
    private static final Pattern maxWeightPattern = Pattern.compile("(.+?)+(?=:)");
    private static final Pattern itemPattern = Pattern.compile("(?<=\\()(.*?)(?=\\))");

    @Override
    public List<BackpackSample> parsePackages(Path filePath) throws IOException {
        String input = FileUtil.readFile(filePath);
        validateInput(input);
        String[] packagesInRows = input.replaceAll(" ", "").split("\\r|\\n");

        List<BackpackSample> backpackSamples = new LinkedList<>();
        for (int i = 0; i < packagesInRows.length; i++) {
            if (!packagesInRows[i].isEmpty()) {
                backpackSamples.add(parseBackpackSample(packagesInRows[i], i));
            }
        }
        if (backpackSamples.size() == 0) {
            throw new InputIllegalArgumentException(String.format("Can't find any valid packages in input: %s", input));
        }
        return backpackSamples;
    }

    private BackpackSample parseBackpackSample(String pack, int rowNumber) {
        int maxWeight = parseMaxWeight(pack, rowNumber);
        List<Item> items = parseItems(pack, rowNumber);
        return new BackpackSample(maxWeight, items);
    }

    private int parseMaxWeight(String pack, int rowNumber) {
        Matcher maxWeightMatcher = maxWeightPattern.matcher(pack);
        if (maxWeightMatcher.find()) {
            return Integer.valueOf(maxWeightMatcher.group());
        } else {
            throw new InputIllegalArgumentException(
                    String.format(
                            "Wrong max weight in row number: %s, row should be like: `1:(1,1.0,€1)`, actual row: %s",
                            rowNumber + 1,
                            pack
                    ));
        }
    }

    private List<Item> parseItems(String pack, int rowNumber) {
        List<Item> items = new LinkedList<>();
        Matcher itemsMatcher = itemPattern.matcher(pack);
        int itemNumber = 0;
        while (itemsMatcher.find()) {
            String item = itemsMatcher.group();
            items.add(parseItem(item, rowNumber, ++itemNumber));
        }
        if (items.size() == 0) {
            throw new InputIllegalArgumentException(
                    String.format("No items found in row number: %s, item should be like: `(1,1.0,€1)`, actual row: %s",
                            rowNumber + 1,
                            pack
                    ));
        }
        return items;
    }

    private Item parseItem(String item, int rowNumber, int itemNumber) {
        String[] itemFields = item.split(",");
        if (itemFields.length != 3) {
            throw new InputIllegalArgumentException(String.format(
                    "Can't parse item in row: %s, items serial number: %s, actual item: %s, item should be like: `(1,1.0,€1)`",
                    rowNumber + 1,
                    itemNumber,
                    item
            ));
        }
        long itemId = parseItemId(itemFields[0], rowNumber, itemNumber);
        double itemWeight = parseItemWeight(itemFields[1], rowNumber, itemId);
        double itemCost = parseItemCost(itemFields[2], rowNumber, itemId);
        return new Item(itemId, itemWeight, itemCost);
    }

    private long parseItemId(String id, int rowNumber, int itemNumber) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException ex) {
            throw new InputIllegalArgumentException(String.format(
                    "Can't parse item id in row: %s, item serial number: %s, actual id: %s, item should be like: `(1,1.0,€1)`",
                    rowNumber + 1,
                    itemNumber,
                    id
            ));
        }
    }

    private double parseItemCost(String cost, int rowNumber, long itemId) {
        try {
            return Double.valueOf(cost.replace("€", ""));
        } catch (NumberFormatException ex) {
            throw new InputIllegalArgumentException(String.format(
                    "Can't parse item cost in row: %s, item id: %s, actual cost: %s, item should be like: `(1,1.0,€1)`",
                    rowNumber + 1,
                    itemId,
                    cost
            ));
        }
    }

    private double parseItemWeight(String weight, int rowNumber, long itemId) {
        try {
            return Double.valueOf(weight);
        } catch (NumberFormatException ex) {
            throw new InputIllegalArgumentException(String.format(
                    "Can't parse item weight in row: %s, item id: %s, actual weight: %s, item should be like: `(1,1.0,€1)`",
                    rowNumber + 1,
                    itemId,
                    weight
            ));
        }
    }

    private void validateInput(String input) {
        if (input.isEmpty() || input.isBlank()) {
            throw new InputIllegalArgumentException("Input file is empty");
        }
    }
}
