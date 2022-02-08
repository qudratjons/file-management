package uz.file.management.mockdata;

import uz.file.management.constants.FileType;
import uz.file.management.constants.ProjectType;
import uz.file.management.entity.ImageEntity;
import uz.file.management.utils.FileUtils;

import java.time.LocalDateTime;

public class ImageTestHelper {

    public static ImageEntity getImageObj() {
        String fileName = FileUtils.fileNameRandomizer("image.jpg");
        ImageEntity image = new ImageEntity()
                .setPath(FileUtils.pathGenerationByDate(FileType.IMAGES, ProjectType.MBGW, fileName))
                .setForeignKey("998944252669")
                .setFileName(fileName)
                .setOrgName("image.jpg")
                .setExtension("jpg")
                .setSizeKByte(2000.0f)
                .setProjectType(ProjectType.MBGW);
        image.setCreatedAt(LocalDateTime.now());
        image.setModifiedAt(LocalDateTime.now());
        return image;
    }
}
