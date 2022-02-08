package uz.file.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // status code = 400
public class RequiredParamException extends Exception {

    public RequiredParamException(String message) {
        super(message);
    }

    public RequiredParamException(String message, Throwable cause) {
        super(message, cause);
    }

}
