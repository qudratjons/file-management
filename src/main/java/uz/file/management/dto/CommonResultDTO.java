package uz.file.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResultDTO implements Serializable {

    private int code;
    private Object data;
    private String message;
    private boolean success;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
