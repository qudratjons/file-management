package uz.file.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

@ResponseStatus(HttpStatus.BAD_REQUEST) // status code = 400
public class FileMustDoesNotEmptyException extends MultipartException {


//    public FileMustDoesNotEmptyException() {
//        super();
//    }

    public FileMustDoesNotEmptyException(String message) {
        super(message);
    }

    public FileMustDoesNotEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}