package uz.file.management.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.file.management.constants.ProjectType;
import uz.file.management.dto.CommonResultDTO;
import uz.file.management.dto.ImageDTO;
import uz.file.management.entity.ImageEntity;
import uz.file.management.exception.*;
import uz.file.management.repositrories.ImageRepository;
import uz.file.management.services.ImageService;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static reactor.netty.Metrics.SUCCESS;
import static uz.file.management.constants.FileType.IMAGES;
import static uz.file.management.utils.FileUtils.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Value("${file.upload-path}")
    private Path path;


    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg", "image/gif");

    @Override
    public CommonResultDTO getAllByForeignKey(String foreignKey) {

        List<ImageDTO> allByForeignKey = imageRepository.findAllByForeignKeyNative(foreignKey).stream().map(ImageEntity::getDTO).collect(Collectors.toList());
        if (allByForeignKey.size() > 0) {
            return new CommonResultDTO(200, allByForeignKey, SUCCESS, true);
        }
        return new CommonResultDTO(204, allByForeignKey, SUCCESS, true);
    }

    @Override
    public CommonResultDTO storeFile(MultipartFile file,
                                     String foreignKey,
                                     boolean isMain,
                                     ProjectType projectType) throws Exception {
        if (Objects.requireNonNull(file.getOriginalFilename()).trim().isEmpty()) {
            throw new FileMustDoesNotEmptyException("File must not be empty. Please try again with image!");
        }
        if (foreignKey == null || foreignKey.isEmpty()) {
            throw new RequiredParamException("Foreign key is required, FK can not be empty");
        }

        String fileContentType = file.getContentType();
        if (contentTypes.contains(fileContentType)) {
            String newFilename = fileNameRandomizer(StringUtils.cleanPath(file.getOriginalFilename()));
            try {
                if (newFilename.contains(".."))
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + newFilename);

                Path targetLocation = this.path.
                        resolve(pathGenerationByDate(IMAGES, projectType, newFilename));

                if (!targetLocation.getParent().toFile().exists())
                    // need to check with ubuntu OS (some problems during package creating/access)
                    targetLocation.getParent().toFile().mkdirs();

                ImageEntity entity = new ImageEntity()
                        .setForeignKey(foreignKey)
                        .setFileName(newFilename)
                        .setProjectType(projectType)
                        .setSizeKByte(file.getSize())
                        .setOrgName(file.getOriginalFilename())
                        .setPath(targetLocation.getParent().toString())
                        .setExtension(file.getOriginalFilename().split("\\.")[1]);
                entity.setCreatedAt(LocalDateTime.now());
                entity.setModifiedAt(LocalDateTime.now());
                ImageEntity save = imageRepository.save(entity);
                rescalingImage(file, save);
                return new CommonResultDTO(200, save.getDTO(), SUCCESS, true);

            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + newFilename + ". Please try again!", new RuntimeException());
            }
        } else {
            throw new WrongFileFormatException("Wrong type file format. Please try again with image!", new RuntimeException());

        }
    }


    @Override
    public Resource loadFileAsResource(String scale, String fileName) throws Exception {
        Optional<ImageEntity> optional = imageRepository.findByFileNameNative(fileName);
        if (optional.isPresent()) {
        return getResource(true, fileName, scale, optional.get().getPath());
        }
        throw new FileNotFoundException("File with given name not found!");
    }

    @Override
    public CommonResultDTO deleteFileById(Long id) throws Exception {
        if (id == null || id <= 0L) throw new RequiredParamException("Missing param 'id'.", new Throwable());
        Optional<ImageEntity> optional = imageRepository.findById(id);
        if (optional.isPresent()) {
            deleteImages(optional.get().getPath(), optional.get().getFileName());
            imageRepository.deleteById(optional.get().getId());
            return new CommonResultDTO(200, true, SUCCESS, true);
        }
        throw new FileNotFoundException("File with id: " + id + " not found!");
    }
}
