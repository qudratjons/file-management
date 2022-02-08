package uz.file.management.services;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import uz.file.management.constants.ProjectType;
import uz.file.management.dto.CommonResultDTO;

public interface ImageService {
    CommonResultDTO getAllByForeignKey(String foreignKey);

    CommonResultDTO storeFile(MultipartFile file, String foreignKey, boolean isMain, ProjectType projectType) throws Exception;

    Resource loadFileAsResource(String scale, String fileName) throws Exception;

    CommonResultDTO deleteFileById(Long id) throws Exception;
}
