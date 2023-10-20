package com.fairgoods.webshop.service;

import com.fairgoods.webshop.model.File;
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
    private final Path uploadDirectory = Paths.get("webshop/webshop/src/main/java/com/fairgoods/webshop/images");

    /**
     * Uploads a file to the predefined directory and returns a file entity.
     *
     * @param file MultipartFile object representing the file to be uploaded.
     * @return A File object containing metadata such as the file’s name.
     */
    @Override
    public File upload(MultipartFile file) {
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

        File fileEntity = new File();
        fileEntity.setFileName(fileName);
        return fileEntity;

    }

    /**
     * Retrieves a file stored in the predefined directory based on its reference (filename).
     *
     * @param reference String representing the filename.
     * @return A Resource object representing the retrieved file.
     */
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

    /**
     * Deletes a file from the predefined directory based on its reference (filename).
     *
     * @param reference String representing the filename to be deleted.
     * @return boolean value indicating the success or failure of the deletion operation.
     */
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

    /**
     * Updates an existing file in the predefined directory. Deletes the existing file and
     * stores the new file in its place.
     *
     * @param reference String representing the filename of the file to be updated.
     * @param file MultipartFile object representing the new file.
     * @return boolean value indicating the success or failure of the update operation.
     */
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