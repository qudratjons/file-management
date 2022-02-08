package uz.file.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import uz.file.management.constants.ProjectType;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_EMPTY)
public class ImageDTO implements Serializable {

    private Long id;
    private String orgName;
    private String fileName;
    private String foreignKey;
    private String extension;
    private float sizeKByte;
    private ProjectType projectType;

}
