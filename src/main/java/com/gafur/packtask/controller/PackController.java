package com.gafur.packtask.controller;

import static com.gafur.packtask.util.EntityValidator.validate;

import com.gafur.packtask.entity.BackpackSample;
import com.gafur.packtask.entity.BackpackSolution;
import com.gafur.packtask.entity.InputIllegalArgumentException;
import com.gafur.packtask.entity.Item;
import com.gafur.packtask.service.PackService;
import com.gafur.packtask.service.ParsingService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javax.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PackController {
    private final PackService packService;
    private final ParsingService parsingService;

    public String packByMaxCost(Path filePath) {
        try {
            List<BackpackSample> backpackSamples = parsingService.parsePackages(filePath);
            validateBackpackSamples(backpackSamples);

            StringBuilder builder = new StringBuilder();
            for (BackpackSample pack : backpackSamples) {
                BackpackSolution solution = packService.packItemsByMaxCost(pack);
                builder.append(solution.toString());
                builder.append("\n");
            }
            return builder.toString();
        } catch (IOException ex) {
            return ex.getMessage() + ", please use valid file path";
        } catch (InputIllegalArgumentException ex) {
            return ex.getMessage() + ", please use valid input file";
        } catch (ConstraintViolationException ex) {
            return "Input file has not valid data: " + ex.getMessage();
        }
    }

    private void validateBackpackSamples(List<BackpackSample> backpackSamples) {
        for (BackpackSample sample : backpackSamples) {
            validate(sample);
            for (Item item : sample.getItems()) {
                validate(item);
            }
        }
    }
}
