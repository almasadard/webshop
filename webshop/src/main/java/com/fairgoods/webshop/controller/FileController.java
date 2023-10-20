package com.fairgoods.webshop.controller;


import com.fairgoods.webshop.model.File;
import com.fairgoods.webshop.repository.FileRepository;
import com.fairgoods.webshop.service.FileService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for managing files (upload, download, delete).
 *
 */
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileRepository fileRepository;

    /**
     * Uploads a file.
     *
     * @param file The file to upload.
     * @return The ID of the uploaded file.
     * @throws IOException If an I/O error occurs.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String fileUpload(@RequestParam("file")MultipartFile file) throws IOException {
        File fileEntity = fileService.upload(file);
        fileRepository.save(fileEntity);

        return fileEntity.getId().toString();
    }

    /**
     * Retrieves a file based on a reference.
     *
     * @param reference The reference to the file.
     * @return The file as a resource, or a 404 not found status if the file doesn't exist.
     */
    @GetMapping("/{reference}")
    public @ResponseBody ResponseEntity<Resource> getFile(@PathVariable String reference) {
        Resource fileResource = fileService.get(reference);

        if (fileResource != null) {
            String filename = fileResource.getFilename();
            String fileExtension = filename.substring(filename.lastIndexOf(".") +1);

            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (fileExtension.equalsIgnoreCase("png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a file based on a reference.
     *
     * @param reference The reference to the file to delete.
     * @return A response entity with a 204 no content status if deletion was successful, or a 404 not found status if the file doesn't exist.
     */
    @DeleteMapping("/{reference}")
    public ResponseEntity<Void> deleteFile (@PathVariable String reference) {
        boolean deleted = fileService.delete(reference);

        if(deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}