package com.example.backend.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStorageUtil {
	public String saveFile(String uploadDir, MultipartFile file) throws IOException {

	    String originalName = file.getOriginalFilename();
	    String extension = originalName.substring(originalName.lastIndexOf("."));
	    String newFileName = UUID.randomUUID().toString() + extension;

	    Path path = Paths.get(uploadDir);
	    System.out.println("Absolute path: " + path.toAbsolutePath());

	    if (!Files.exists(path)) {
	        Files.createDirectories(path);
	    }

	    Path filePath = path.resolve(newFileName);

	    try (InputStream inputStream = file.getInputStream()) {
	        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	    }

	    return newFileName;
	}


	
	public void deleteFile(String uploadDir, String fileName) {

        if (fileName == null) return;

        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace(); // replace with logger in production
        }
    }
}
