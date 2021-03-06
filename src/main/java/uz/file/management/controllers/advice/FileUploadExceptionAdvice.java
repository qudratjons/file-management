package uz.file.management.controllers.advice;

import uz.file.management.dto.CommonResultDTO;
import uz.file.management.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import uz.file.management.services.TranslationService;

@Slf4j
@ControllerAdvice
public class FileUploadExceptionAdvice {

    private final TranslationService translationService;

    public FileUploadExceptionAdvice(TranslationService translationService) {
        this.translationService = translationService;
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<CommonResultDTO> fileNotFoundException(FileNotFoundException exc) {
        log.warn("----------   Error: {}   ----------", exc.getMessage());
        CommonResultDTO result = new CommonResultDTO(400, null, exc.getMessage(), false);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @ExceptionHandler(FileMustDoesNotEmptyException.class)
    public ResponseEntity<CommonResultDTO> fileMustDoesNotEmptyException(FileMustDoesNotEmptyException exc) {
        log.warn("----------   Error: {}   ----------", exc.getMessage());
        CommonResultDTO result = new CommonResultDTO(400, null, exc.getMessage(), false);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<CommonResultDTO> fileStorageException(FileStorageException exc) {
        log.warn("----------   Error: {}   ----------", exc.getMessage());
        CommonResultDTO result = new CommonResultDTO(406, null, exc.getMessage(), false);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @ExceptionHandler(WrongFileFormatException.class)
    public ResponseEntity<CommonResultDTO> wrongFileFormatException(WrongFileFormatException exc) {
        log.warn("----------   Error: {}   ----------", exc.getMessage());
        CommonResultDTO result = new CommonResultDTO(409, null, exc.getMessage(), false);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonResultDTO> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        log.warn("----------   Error: {}   ----------", exc.getMessage());
        CommonResultDTO result = new CommonResultDTO(413, null, exc.getMessage(), false);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @ExceptionHandler(BundleException.class)
    public ResponseEntity<CommonResultDTO> handleMaxSizeException(BundleException exc) {
        log.warn("----------   Error: {}   ----------", exc.getMessage());

        String translate = translationService.translate(exc.getMessage());
        CommonResultDTO result = new CommonResultDTO(413, null, translate, false);
        return ResponseEntity.status(result.getCode()).body(result);
    }

}