package uz.file.management.controllers.advice;


import uz.file.management.dto.CommonResultDTO;
import uz.file.management.exception.RequiredParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class RectControllerAdvices {

    @ExceptionHandler(value = {IOException.class})
    public static ResponseEntity<Resource> getResourceResponseEntity(HttpServletRequest request, Resource resource) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.warn("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @ExceptionHandler(RequiredParamException.class)
    public ResponseEntity<CommonResultDTO> requiredParamException(RequiredParamException exc) {
        log.warn("----------   Error: {}   ----------", exc.getMessage());
        CommonResultDTO result = new CommonResultDTO(400, null, exc.getMessage(), false);
        return ResponseEntity.status(result.getCode()).body(result);
    }
}