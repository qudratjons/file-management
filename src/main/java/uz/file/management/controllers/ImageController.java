package uz.file.management.controllers;

import uz.file.management.constants.ProjectType;
import uz.file.management.dto.CommonResultDTO;
import uz.file.management.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static uz.file.management.controllers.advice.RectControllerAdvices.getResourceResponseEntity;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/file/images")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("{foreignKey}")
    public ResponseEntity<CommonResultDTO> getAllByForeignKey(@PathVariable String foreignKey) {
        CommonResultDTO resultDTO = imageService.getAllByForeignKey(foreignKey);
        return ResponseEntity.status(resultDTO.getCode()).body(resultDTO);
    }

    @PostMapping("/upload")
    public ResponseEntity<CommonResultDTO> uploadFile(@RequestParam("file") @Validated MultipartFile file,
                                                      @RequestParam(name = "foreignKey") String foreignKey,
                                                      @RequestParam(name = "projectType", defaultValue = "OTHERS") ProjectType projectType,
                                                      @RequestParam(name = "isMain", defaultValue = "false") boolean isMain) throws Exception {
        CommonResultDTO result = imageService.storeFile(file, foreignKey, isMain, projectType);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping("/upload-multiple-files")
    public ResponseEntity<List<CommonResultDTO>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                                                     @RequestParam(name = "projectType", defaultValue = "OTHERS") ProjectType projectType,
                                                                     @RequestParam(name = "foreignKey") String foreignKey) {
        return ResponseEntity.ok(Arrays.stream(files)
                .map(multipartFile -> {
                    try {
                        return imageService.storeFile(multipartFile, foreignKey, false, projectType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList()));
    }

    @GetMapping("/download/{scale}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String scale,
                                                 @PathVariable String fileName,
                                                 HttpServletRequest request) throws Exception {
        Resource resource = imageService.loadFileAsResource(scale, fileName);
        return getResourceResponseEntity(request, resource);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommonResultDTO> deleteFileByID( @PathVariable Long id) throws Exception {
        CommonResultDTO resultDTO = imageService.deleteFileById(id);
        return ResponseEntity.status(resultDTO.getCode()).body(resultDTO);
    }

}
