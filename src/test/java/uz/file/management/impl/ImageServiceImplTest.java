package uz.file.management.impl;

import uz.file.management.constants.ProjectType;
import uz.file.management.dto.CommonResultDTO;
import uz.file.management.dto.ImageDTO;
import uz.file.management.entity.ImageEntity;
import uz.file.management.exception.*;
import uz.file.management.repositrories.ImageRepository;
import uz.file.management.services.impl.ImageServiceImpl;
import uz.file.management.utils.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static uz.file.management.mockdata.ImageTestHelper.getImageObj;


@ExtendWith(SpringExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    private ImageServiceImpl underTest;

    @Mock
    private ImageRepository imageRepository;

    private ImageEntity imageObj = getImageObj();

    @BeforeEach
    void setUp() {
        BDDMockito.when(imageRepository.findAllByForeignKeyNative(ArgumentMatchers.anyString()))
                .thenReturn(List.of(imageObj));

        BDDMockito.when(imageRepository.findByFileNameNative(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(imageObj));

        BDDMockito.when(imageRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(imageObj));
    }

    @Test
    @DisplayName("SUCCESS: Find all images by ForeignKey")
    void getAllByForeignKey_it_sholud_return_all_images_by_foreignkey() {

        CommonResultDTO result = underTest.getAllByForeignKey("998944252669");

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getData());
        Assertions.assertNotNull(result.getCode(), String.valueOf(200));
        Assertions.assertNotNull(result.getMessage(), "SUCCESS");

        List<ImageDTO> list = (List<ImageDTO>) result.getData();
        Assertions.assertNotNull(list);
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertTrue(list.size() == 1);
        Assertions.assertTrue(list.get(0).getForeignKey() == "998944252669");
    }

    @Test
    @DisplayName("FAIL: No images with ForeignKey")
    void getAllByForeignKey_it_should_return_emptyList_with_statusCode_204() {

        BDDMockito.when(imageRepository.findAllByForeignKeyNative(ArgumentMatchers.anyString()))
                .thenReturn(List.of());

        CommonResultDTO result = underTest.getAllByForeignKey("1L");

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getData());
        Assertions.assertNotNull(result.getCode(), String.valueOf(204));
        Assertions.assertNotNull(result.getMessage(), "SUCCESS");

        List<ImageDTO> list = (List<ImageDTO>) result.getData();
        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("SUCCESS: Delete image by Id")
    public void deleteFileById_it_should_delete_image_without_error() throws Exception {

        MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class);

        filesMockedStatic.when(() -> Files.delete(ArgumentMatchers.any()))
                .thenAnswer((Answer<Void>) invocation -> null);

        CommonResultDTO result = underTest.deleteFileById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getData());
        Assertions.assertEquals(result.getData(), true);
        Assertions.assertNotNull(result.getMessage(), "SUCCESS");
        Assertions.assertNotNull(result.getCode(), String.valueOf(200));
    }

    @Test
    @DisplayName("FAIL: Delete image by Id (Image not found in given Path)")
    void deleteFileById_it_should_throw_FileStorageException() throws Exception {

        try {
            MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class, new Returns(new IOException()));

            filesMockedStatic.when(() -> Files.delete(ArgumentMatchers.any(Path.class)))
                    .thenThrow(new IOException());

            Assertions.assertThrows(FileStorageException.class, () -> underTest.deleteFileById(1L),
                    "File name: " + imageObj.getFileName() + " not found");
        } catch (Exception e) {
        }
    }

    @Test
    @DisplayName("FAIL: Delete image by id (Invalid imageId)")
    void deleteFileById_it_should_throw_RequiredParamException() throws Exception {

        Assertions.assertThrows(RequiredParamException.class,
                () -> underTest.deleteFileById(-1L), "Missing param 'id'.");

    }

    @Test
    @DisplayName("Fail: Delete image by Id (Image not found in DB)")
    void deleteFileById_it_should_throw_FileNotFoundException() throws Exception {

        BDDMockito.when(imageRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Long imageId = 1L;

        Assertions.assertThrows(FileNotFoundException.class,
                () -> underTest.deleteFileById(imageId), "File with id: " + imageId + " not found!");
    }

    @Test
    @DisplayName("SUCCESS: Download image")
    void loadFileAsResource_it_should_return_image_without_error() throws Exception {
        MockedStatic<FileUtils> fileUtilsMockedStatic = Mockito.mockStatic(FileUtils.class);

        fileUtilsMockedStatic.when(() -> FileUtils.getResource(true, imageObj.getFileName(), "large", imageObj.getPath()))
                .thenReturn(new ByteArrayResource(new byte[2]));

        Resource result = underTest.loadFileAsResource("large", imageObj.getFileName());

        Assertions.assertNotNull(result);

    }

    @Test
    @DisplayName("FAIL: Image with geven name not found (Image not found in DB)")
    void loadFileAsResource_it_should_throw_FileNotFoundException() throws Exception {

        BDDMockito.when(imageRepository.findByFileNameNative(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(FileNotFoundException.class,
                () -> underTest.loadFileAsResource("large", imageObj.getFileName()), "File with given name not found!");
    }

    @Test
    @DisplayName("FAIL: Image with geven name not found (Image not found in given Path)")
    void loadFileAsResource_it_should_throw_Exception() throws Exception {

        Assertions.assertThrows(FileNotFoundException.class,
                () -> underTest.loadFileAsResource("large", imageObj.getFileName()), "File not found " + imageObj.getFileName());
    }

    @Test
    @DisplayName("FAIL: Upload image (File is empty)")
    void storeFile_it_should_throw_FileMustDoesNotEmptyException() throws Exception {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "", "", new byte[0]);

        Assertions.assertThrows(FileMustDoesNotEmptyException.class,
                () -> underTest.storeFile(mockMultipartFile, "", false, ProjectType.MBGW),
                "File must not be empty. Please try again with image!");
    }

    @Test
    @DisplayName("FAIL: Upload image (Foreign key is empty)")
    void storeFile_it_should_throw_RequiredParamException() throws Exception {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.jpg", "image/jpg", new byte[0]);

        Assertions.assertThrows(RequiredParamException.class,
                () -> underTest.storeFile(mockMultipartFile, "", false, ProjectType.MBGW),
                "Foreign key is required, FK can not be empty");

    }

    @Test
    @DisplayName("FAIL: Upload image (Wrong file format)")
    void storeFile_it_should_throw_WrongFileFormatException() throws Exception {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "file.zip", "application/zip", new byte[8]);

        Assertions.assertThrows(WrongFileFormatException.class,
                () -> underTest.storeFile(mockMultipartFile, "998944252669", false, ProjectType.MBGW));
    }
}