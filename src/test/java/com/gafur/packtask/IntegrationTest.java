package com.gafur.packtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTest {

    protected Path createTempFile(String input) throws IOException {
        Path filePath = Files.createTempFile(
                Path.of("src/test/resources/input/temp"),
                "test",
                "txt"
        );
        Files.write(filePath, input.getBytes(StandardCharsets.UTF_8));
        return filePath;
    }
}
