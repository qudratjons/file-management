package uz.file.management.repositrories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.file.management.entity.ImageEntity;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    @Query(value = "SELECT * FROM file_ms_images WHERE foreign_key = :foreignKey", nativeQuery = true)
    List<ImageEntity> findAllByForeignKeyNative(@Param("foreignKey") String foreignKey);

    @Query( value = "SELECT * FROM file_ms_images WHERE file_name = :fileName",nativeQuery = true)
    Optional<ImageEntity> findByFileNameNative(@Param("fileName") String fileName);
}
