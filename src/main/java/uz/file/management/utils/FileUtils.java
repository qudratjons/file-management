package uz.file.management.utils;

import uz.file.management.constants.FileType;
import uz.file.management.constants.ImageScaling;
import uz.file.management.constants.ProjectType;
import uz.file.management.entity.ImageEntity;
import uz.file.management.exception.FileNotFoundException;
import uz.file.management.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class FileUtils {

    public static String fileNameRandomizer(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return UUID.randomUUID() + "." + extension;
    }

    // use this method with thumbnails dependency
    public static void photoResizer(Path targetLocation, Path serviceDir, ImageScaling[] scale) throws IOException {
        log.info("creating time: {}", LocalDateTime.now());
        for (int i = 0; i < scale.length; i++) {
            int size = (2 * i + 1) * 200;
            File file = new File(serviceDir.toFile() + File.separator + scale[i].name());
            if (!file.exists()) file.mkdirs();
            Thumbnails.of(targetLocation.toFile()).size(size, size).toFiles(file, Rename.NO_CHANGE);
            log.info("scale: {}, done at: {}", scale[i].name(), LocalDateTime.now());
        }
    }


    // java core base method
    public static void rescalingImage(MultipartFile file, ImageEntity save) throws IOException {

        BufferedImage origin = ImageIO.read(file.getInputStream());
        float scale = (float) origin.getWidth() / (float) origin.getHeight();
        for (int i = 0; i < ImageScaling.values().length; i++) {
            int height = Math.min((2 * i + 1) * 200, origin.getHeight());
            int width = Math.min((int) (height * scale), origin.getWidth());

            Image resultingImage = origin.getScaledInstance(width, height, Image.SCALE_FAST);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(resultingImage, 0, 0, null);
            File output = new File(save.getPath() + File.separator + ImageScaling.values()[i].name() + File.separator + save.getFileName());
            if (!output.exists()) output.mkdirs();
            ImageIO.write(bufferedImage, save.getExtension(), output);
        }

    }

    public static String pathGenerationByDate(FileType fileType, ProjectType projectType, String newFilename) {
        String format = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MMMM-dd"));
        String[] strings = format.split("-");
        StringBuilder result = new StringBuilder();
        for (String date : strings)
            result.append(date)
                    .append(File.separator);
        return fileType.name() + File.separator + projectType.name() + File.separator + result + newFilename;
    }

    public static void deleteImages(String path, String fileName) throws FileStorageException {
        for (ImageScaling scaling : ImageScaling.values()) {
            Path filePath = Paths.get(path + File.separator + scaling.name() + File.separator + fileName);
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new FileStorageException("File name: " + fileName + " not found", e);
            }
        }
    }

    public static Resource getResource(boolean present, String fileName, String scale, String path) throws Exception {
        if (present) {
            File file;
            Path filePath = Path.of(path);
            if (scale.equalsIgnoreCase("ORIGINAL")) {
                filePath = filePath.resolve(fileName).normalize();
            } else {
                file = new File(filePath + File.separator + scale.toUpperCase());
                filePath = file.toPath().resolve(fileName).normalize();
            }
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
        }
        throw new FileNotFoundException("File not found " + fileName);
    }

}
