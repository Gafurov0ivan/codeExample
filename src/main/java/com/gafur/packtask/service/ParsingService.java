package com.gafur.packtask.service;

import com.gafur.packtask.entity.BackpackSample;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ParsingService {
    
    List<BackpackSample> parsePackages(Path filePath) throws IOException;
}
