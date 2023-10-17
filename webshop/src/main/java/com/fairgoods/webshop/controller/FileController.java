package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.FileUploadResponse;
import com.fairgoods.webshop.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public @ResponseBody FileUploadResponse upload(
            @RequestParam("file") MultipartFile file
    ) {
        String reference = fileService.upload(file);

        return new FileUploadResponse(
                true, reference
        );
    }

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