package com.gafur.packtask;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.gafur.packtask.entity.BackpackSample;
import com.gafur.packtask.entity.BackpackSolution;
import com.gafur.packtask.entity.Item;
import com.gafur.packtask.service.PackService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PackServiceTest {

    @Autowired
    private PackService packService;

    @Test
    public void packWithZeroMaxWeight() {
        assertThat(
                packService.packItemsByMaxCost(new BackpackSample(0, generateItemList(new Item(1, 1.0, 1.0)))).getItems().size(),
                equalTo(0)
        );
    }

    @Test
    public void packWithEqualMaxWeight() {
        assertThat(
                packService.packItemsByMaxCost(new BackpackSample(1, generateItemList(new Item(1, 1.0, 1.0)))).getItems().size(),
                equalTo(1)
        );
    }

    @Test
    public void packWithGreaterWeight() {
        assertThat(
                packService.packItemsByMaxCost(new BackpackSample(1, generateItemList(new Item(1, 1.1, 1.0)))).getItems().size(),
                equalTo(0)
        );
    }

    @Test
    public void packWithoutItems() {
        assertThat(packService.packItemsByMaxCost(new BackpackSample(1, new ArrayList<>())).getItems().size(), equalTo(0));
    }

    @Test
    public void packDoubleWeight() {
        
        BackpackSolution solution = packService.packItemsByMaxCost(
                new BackpackSample(5, generateItemList(
                        new Item(1, 1.01, 1.0),
                        new Item(2, 1.0, 1.0),
                        new Item(3, 1.01, 1.0),
                        new Item(4, 1.0, 1.0),
                        new Item(5, 1.01, 1.0),
                        new Item(6, 1.0, 1.0),
                        new Item(7, 1.01, 1.0),
                        new Item(8, 1.0, 1.0),
                        new Item(9, 1.0, 1.0),
                        new Item(10, 1.01, 1.0)
                ))
        );
        assertThat(
                solution.getItems().stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(",")), 
                equalTo("2,4,6,8,9"));
    }

    @Test
    public void packDoubleCost() {
        BackpackSolution solution = packService.packItemsByMaxCost(
                new BackpackSample(5, generateItemList(
                        new Item(1, 1.0, 1.01),
                        new Item(2, 1.0, 1.0),
                        new Item(3, 1.0, 1.01),
                        new Item(4, 1.0, 1.0),
                        new Item(5, 1.0, 1.01),
                        new Item(6, 1.0, 1.0),
                        new Item(7, 1.0, 1.01),
                        new Item(8, 1.0, 1.0),
                        new Item(9, 1.0, 1.0),
                        new Item(10, 1.0, 1.01)
                ))
        );
        assertThat(
                solution.getItems().stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(",")),
                equalTo("1,3,5,7,10"));
    }

    @Test
    public void pack15ItemsByCost() {
        BackpackSolution solution = packService.packItemsByMaxCost(
                new BackpackSample(100, generateItemList(
                        new Item(1, 1.0, 1.01),
                        new Item(2, 1.0, 1.0),
                        new Item(3, 1.0, 1.01),
                        new Item(4, 1.0, 1.0),
                        new Item(5, 1.0, 1.01),
                        new Item(6, 1.0, 1.0),
                        new Item(7, 1.0, 1.01),
                        new Item(8, 1.0, 1.0),
                        new Item(9, 1.0, 1.0),
                        new Item(10, 1.0, 1.01),
                        new Item(11, 1.0, 1.0),
                        new Item(12, 1.0, 1.01),
                        new Item(13, 1.0, 1.0),
                        new Item(14, 1.0, 1.01),
                        new Item(15, 1.0, 1.0),
                        new Item(16, 1.0, 1.01)
                ))
        );
        assertThat(solution.getItems().stream().mapToDouble(Item::getCost).sum(), equalTo(15.08d));
        assertThat(solution.getItems().stream().mapToDouble(Item::getWeight).sum(), equalTo(15d));
        assertThat(solution.getItems().size(), equalTo(15));
    }

    @Test
    public void pack15ItemsByWeight() {
        BackpackSolution solution = packService.packItemsByMaxCost(
                new BackpackSample(100, generateItemList(
                        new Item(1, 0.9, 1.0),
                        new Item(2, 1.0, 1.0),
                        new Item(3, 1.0, 1.0),
                        new Item(4, 1.0, 1.0),
                        new Item(5, 1.0, 1.0),
                        new Item(6, 1.0, 1.0),
                        new Item(7, 1.0, 1.0),
                        new Item(8, 1.0, 1.0),
                        new Item(9, 1.0, 1.0),
                        new Item(10, 1.0, 1.0),
                        new Item(11, 1.0, 1.0),
                        new Item(12, 1.0, 1.0),
                        new Item(13, 1.0, 1.0),
                        new Item(14, 1.0, 1.0),
                        new Item(15, 1.0, 1.0),
                        new Item(16, 0.9, 1.0)
                ))
        );
        assertThat(solution.getItems().stream().mapToDouble(Item::getCost).sum(), equalTo(15d));
        assertThat(solution.getItems().stream().mapToDouble(Item::getWeight).sum(), equalTo(14.8));
        assertThat(solution.getItems().size(), equalTo(15));
    }

    private List<Item> generateItemList(Item... item) {
        return new ArrayList<>(Arrays.asList(item));
    }
}
