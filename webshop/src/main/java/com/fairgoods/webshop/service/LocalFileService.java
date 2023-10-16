package com.fairgoods.webshop.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileService implements FileService {
    private final Path uploadDirectory = Paths.get("images");

    @Override
    public String upload(MultipartFile file) {
        if (!Files.exists(uploadDirectory)) {
            try {
                Files.createDirectories(uploadDirectory);
            } catch (IOException e) {
                throw new RuntimeException("Error creating directory", e);
            }
        }
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = uploadDirectory.resolve(fileName);

        try {
            Files.copy(file.getInputStream(), uploadPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;

    }

    @Override
    public Resource get(String reference) {
        try {
            Path filePath = uploadDirectory.resolve(reference).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("The file was not found or cannot be read.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error retrieving the file: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String reference) {
        try {
            Path filePath = uploadDirectory.resolve(reference).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                Files.delete(filePath);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting the file: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(String reference, MultipartFile file) {
        try {
            Path filePath = uploadDirectory.resolve(reference).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                Files.delete(filePath);

                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path uploadPath = uploadDirectory.resolve(fileName);
                Files.copy(file.getInputStream(), uploadPath);

                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error updating the file: " + e.getMessage(), e);
        }
    }

}