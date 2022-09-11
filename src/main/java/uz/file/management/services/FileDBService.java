package uz.file.management.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileDBService {

    String uploadFile(MultipartFile file) throws IOException;

    ResponseEntity<byte[]> downloadImage(String fileName);

}

