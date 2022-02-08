package uz.file.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

@ResponseStatus(HttpStatus.CONFLICT) // status code = 409
public class WrongFileFormatException extends MultipartException {
//    public WrongFileFormatException(String message,  @Nullable Throwable ex) {
//        super(message);
//    }

    public WrongFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}