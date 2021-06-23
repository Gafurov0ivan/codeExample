package com.gafur.packtask.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public static String readFile(Path path) throws IOException {
        Charset encoding = Charset.forName(UTF_8.name());
        String content = "";
        try {
            byte[] encoded = Files.readAllBytes(path);
            content = new String(encoded, encoding);
        } catch (IOException e) {
            throw new IOException("Can't read file in path: " + path.toString(), e);
        }
        return content;
    }
}
