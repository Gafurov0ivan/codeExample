package com.gafur.packtask;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gafur.packtask.entity.BackpackSample;
import com.gafur.packtask.entity.InputIllegalArgumentException;
import com.gafur.packtask.entity.Item;
import com.gafur.packtask.service.ParsingService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ParsingServiceTest extends IntegrationTest {

    @Autowired
    private ParsingService parsingService;

    @AfterEach
    public void cleanUp() {
        Arrays.stream(new File("src/test/resources/input/temp").listFiles()).forEach(File::delete);
    }

    @Test
    public void parseValidInput() throws IOException {
        List<BackpackSample> backpackSamples = parsingService.parsePackages(Path.of("src/test/resources/input/validInput.txt"));
        assertThat(backpackSamples.size(), equalTo(4));
        BackpackSample pack = backpackSamples.get(0);
        assertThat(pack.getMaxWeight(), equalTo(81));
        assertThat(pack.getItems().size(), equalTo(6));
        Item item = pack.getItems().get(0);
        assertThat(item.getId(), equalTo(1L));
        assertEquals(item.getWeight(), 53.38d);
        assertEquals(item.getCost(), 45d);
    }

    @Test
    public void parseWeirdFormatInput() throws IOException {
        List<BackpackSample> backpackSamples = parsingService.parsePackages(Path.of("src/test/resources/input/weirdFormatInput.txt"));
        assertThat(backpackSamples.size(), equalTo(2));
        BackpackSample pack = backpackSamples.get(0);
        assertThat(pack.getMaxWeight(), equalTo(81));
        assertThat(pack.getItems().size(), equalTo(6));
        Item item = pack.getItems().get(0);
        assertThat(item.getId(), equalTo(1L));
        assertEquals(item.getWeight(), 53.38d);
        assertEquals(item.getCost(), 45d);
    }

    @Test
    public void parseEmptyInput() {
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () ->
                parsingService.parsePackages(Path.of("src/test/resources/input/emptyInput.txt"))
        );
        assertThat(ex.getMessage(), equalTo("Input file is empty"));
    }

    @Test
    public void cantParseFile() {
        IOException ex = assertThrows(IOException.class, () -> parsingService.parsePackages(Path.of("notValidPath.txt")));
        assertThat(ex.getMessage(), equalTo("Can't read file in path: notValidPath.txt"));
    }

    @Test
    public void notValidItemFormat() throws IOException {
        String input = "1:1,1.0,€1)(2,1.0,€1(3,1.0,€1)";
        Path path = createTempFile(input);
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () -> parsingService.parsePackages(path));
        assertThat(
                ex.getMessage(),
                equalTo("Can't parse item in row: 1, items serial number: 1, "
                        + "actual item: 2,1.0,€1(3,1.0,€1, item should be like: `(1,1.0,€1)`")
        );
    }

    @Test
    public void parseInputWithoutMaxWeight() throws IOException {
        String input = ":(1,1.0,€1)";
        Path path = createTempFile(input);
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () -> parsingService.parsePackages(path));
        assertThat(
                ex.getMessage(),
                equalTo("Wrong max weight in row number: 1, row should be like: `1:(1,1.0,€1)`, actual row: " + input)
        );
    }

    @Test
    public void parseWrongFormatMaxWeight() throws IOException {
        String input = "1(1,1.0,€1)";
        Path path = createTempFile(input);
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () -> parsingService.parsePackages(path));
        assertThat(
                ex.getMessage(),
                equalTo("Wrong max weight in row number: 1, row should be like: `1:(1,1.0,€1)`, actual row: " + input)
        );
    }

    @Test
    public void parsePackageWithoutItems() throws IOException {
        String input = "1:";
        Path path = createTempFile(input);
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () -> parsingService.parsePackages(path));
        assertThat(
                ex.getMessage(),
                equalTo("No items found in row number: 1, item should be like: `(1,1.0,€1)`, actual row: " + input)
        );
    }

    @Test
    public void parseEmptyItemField() throws IOException {
        String input = "1:(1.0,€1)";
        Path path = createTempFile(input);
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () -> parsingService.parsePackages(path));
        assertThat(
                ex.getMessage(),
                equalTo("Can't parse item in row: 1, items serial number: 1, actual item: 1.0,€1, item should be like: `(1,1.0,€1)`")
        );
    }

    @Test
    public void parseDuplicatesItemField() throws IOException {
        String input = "1:(1,2,1.0,€1)";
        Path path = createTempFile(input);
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () -> parsingService.parsePackages(path));
        assertThat(
                ex.getMessage(),
                equalTo("Can't parse item in row: 1, items serial number: 1, actual item: 1,2,1.0,€1, item should be like: `(1,1.0,€1)`")
        );
    }

    @Test
    public void parseWrongItemId() throws IOException {
        String input = "1:(notValid,1.0,€1)";
        Path path = createTempFile(input);
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () -> parsingService.parsePackages(path));
        assertThat(
                ex.getMessage(),
                equalTo("Can't parse item id in row: 1, item serial number: 1, actual id: notValid, item should be like: `(1,1.0,€1)`")
        );
    }

    @Test
    public void parseWrongItemWeight() throws IOException {
        String input = "1:(1,notValid,€1)";
        Path path = createTempFile(input);
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () -> parsingService.parsePackages(path));
        assertThat(
                ex.getMessage(),
                equalTo("Can't parse item weight in row: 1, item id: 1, actual weight: notValid, item should be like: `(1,1.0,€1)`")
        );
    }

    @Test
    public void parseWrongItemCost() throws IOException {
        String input = "1:(1,1.0,€notValid)";
        Path path = createTempFile(input);
        InputIllegalArgumentException ex = assertThrows(InputIllegalArgumentException.class, () -> parsingService.parsePackages(path));
        assertThat(
                ex.getMessage(),
                equalTo("Can't parse item cost in row: 1, item id: 1, actual cost: €notValid, item should be like: `(1,1.0,€1)`")
        );
    }

    private void assertEquals(double src, double valueToCompare) {
        assertThat(Double.compare(src, valueToCompare), equalTo(0));
    }
}
