package com.gafur.packtask;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.gafur.packtask.controller.PackController;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PackControllerTest extends IntegrationTest {

    @Autowired
    private PackController controller;

    @Test
    public void packByMaxCost() {
        String response = controller.packByMaxCost(Path.of("src/test/resources/input/validInput.txt"));
        assertThat(response, equalTo("4\n-\n2,7\n8,9\n"));
    }

    @Test
    public void packMoreThanFifteenItems() {
        String response = controller.packByMaxCost(Path.of("src/test/resources/input/fifteenItemsMaxLimitInput.txt"));
        assertThat(response, equalTo("1,2,3,4,5,6,7,8,10,11,12,13,14,15,16\n"));
    }

    @Test
    public void cornerCases() throws IOException {
        String input = "100:(2147483648,100.0,€100)";
        Path path = createTempFile(input);
        assertThat(controller.packByMaxCost(path), equalTo("2147483648\n"));
    }

    @Test
    public void constraintsMessageForBackpackSample() throws IOException {
        String input = "101:(1,1.0,€1)";
        Path path = createTempFile(input);
        assertThat(
                controller.packByMaxCost(path),
                equalTo("Input file has not valid data: maxWeight: must be less than or equal to 100")
        );
    }

    @Test
    public void constraintsMessageForItem() throws IOException {
        String input = "1:(-1,1.0,€1)";
        Path path = createTempFile(input);
        assertThat(
                controller.packByMaxCost(path),
                equalTo("Input file has not valid data: id: must be greater than or equal to 1")
        );
    }

    @Test
    public void inputFileNotFoundMessage() {
        String response = controller.packByMaxCost(Path.of("notValidPath"));
        assertThat(response, equalTo("Can't read file in path: notValidPath, please use valid file path"));
    }

    @Test
    public void invalidInputMessage() {
        String response = controller.packByMaxCost(Path.of("src/test/resources/input/emptyInput.txt"));
        assertThat(response, equalTo("Input file is empty, please use valid input file"));
    }
}
