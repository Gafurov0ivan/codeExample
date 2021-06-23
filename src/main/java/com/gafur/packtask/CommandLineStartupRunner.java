package com.gafur.packtask;

import com.gafur.packtask.controller.PackController;
import java.nio.file.Path;
import java.util.Scanner;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Profile("!test")
public class CommandLineStartupRunner implements CommandLineRunner {

    private final PackController controller;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n Please insert path for input file \n");
        String filePath = null;
        while (scanner.hasNext()) {
            filePath = scanner.nextLine();
            System.out.println(controller.packByMaxCost(Path.of(filePath)));
        }
    }
}
