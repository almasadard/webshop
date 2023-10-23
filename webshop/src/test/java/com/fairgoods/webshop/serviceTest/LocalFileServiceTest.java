package com.fairgoods.webshop.serviceTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.model.File;
import com.fairgoods.webshop.service.LocalFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WebshopApplication.class)
@ActiveProfiles("test")
public class LocalFileServiceTest {

    @Autowired
    private LocalFileService localFileService;

    private MultipartFile testFile;

    @BeforeEach
    void setup() {
        // Erstellt eine Mock-Datei, die in den Tests verwendet wirdx
        testFile = new MockMultipartFile(
                "file",
                "filename.txt",
                "text/plain",
                "some xml".getBytes());
    }

    @Test
    void uploadTest() {
        // Testet, ob die Datei erfolgreich hochgeladen wird und eine gültige Datei-Entität zurückgegeben wird
        File file = assertDoesNotThrow(() -> localFileService.upload(testFile));
        assertNotNull(file);
        assertNotNull(file.getFileName());
    }

    @Test
    void getTest() {
        // Testet, ob eine hochgeladene Datei erfolgreich abgerufen werden kann
        File uploadedFile = assertDoesNotThrow(() -> localFileService.upload(testFile));
        Resource file = assertDoesNotThrow(() -> localFileService.get(uploadedFile.getFileName()));
        assertNotNull(file);
    }

    @Test
    void deleteTest() {
        // Testet, ob eine hochgeladene Datei erfolgreich gelöscht werden kann
        File uploadedFile = assertDoesNotThrow(() -> localFileService.upload(testFile));
        boolean isDeleted = localFileService.delete(uploadedFile.getFileName());
        assertTrue(isDeleted);
    }

    @Test
    void updateTest(){
        // Testet, ob eine hochgeladene Datei erfolgreich aktualisiert werden kann

        File uploadedFile = assertDoesNotThrow(() -> localFileService.upload(testFile));

        MultipartFile newTestFile = new MockMultipartFile("file", "new_filename.txt", "text/plain", "some new xml".getBytes());
        boolean isUpdated = localFileService.update(uploadedFile.getFileName(), newTestFile);
        assertTrue(isUpdated);
    }
}
