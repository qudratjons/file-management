package uz.file.management.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import uz.file.management.entity.FileDB;
import uz.file.management.repositrories.FileDBRepository;
import uz.file.management.services.FileDBService;
import uz.file.management.utils.FileCompress;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class FileDBServiceImpl implements FileDBService {

    private final FileDBRepository fileDBRepository;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {

        FileDB fileDB = new FileDB();

        fileDB.setName(file.getOriginalFilename());
        fileDB.setType(file.getContentType());
        fileDB.setFileInByte(FileCompress.compressImage(file.getBytes()));

        FileDB saved = fileDBRepository.save(fileDB);

        log.info("File uploaded successfully: {}", saved.getName());

        return saved.getName();
    }

    @Override
    public ResponseEntity<byte[]> downloadImage(String fileName) {
        return fileDBRepository.findByName(fileName)
                .map(fileDB -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, fileDB.getType())
                        .body(FileCompress.decompressImage(fileDB.getFileInByte()))
                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "file not found"));

    }
}
