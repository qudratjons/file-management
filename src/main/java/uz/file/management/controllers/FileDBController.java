package uz.file.management.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.file.management.services.FileDBService;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/file")
public class FileDBController {

    private final FileDBService fileDBService;

    @PostMapping("/upload")
    public String upload(@RequestParam(name = "file") MultipartFile file) throws IOException {
        return fileDBService.uploadFile(file);
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<byte[]> upload(@PathVariable(name = "name") String name) {
        return fileDBService.downloadImage(name);
    }
}
