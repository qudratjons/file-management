package uz.file.management.repositrories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.file.management.entity.FileDB;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface FileDBRepository extends JpaRepository<FileDB, Long> {

    Optional<FileDB> findByName(String name);

}
