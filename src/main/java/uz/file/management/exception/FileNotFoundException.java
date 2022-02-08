package uz.file.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.NotContextException;

@ResponseStatus(HttpStatus.BAD_REQUEST) // status code = 400
public class FileNotFoundException extends NotContextException {
    public FileNotFoundException(String message) {
        super(message);
    }
}