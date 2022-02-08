package uz.file.management.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import uz.file.management.constants.ProjectType;
import uz.file.management.dto.ImageDTO;
import uz.file.management.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

import static uz.file.management.constants.TableName.FILE_MS_IMAGES;

@Getter
@Setter
@Entity
@ToString
@Accessors(chain = true)
@RequiredArgsConstructor
@Table(name = FILE_MS_IMAGES)
public class ImageEntity extends BaseEntity {

    private String path;

    @Column(name = "foreign_key")
    private String foreignKey;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "org_name")
    private String orgName;

    private String extension;

    @Column(name = "size_kb")
    private float sizeKByte;

    @Enumerated(EnumType.STRING)
    private ProjectType projectType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ImageEntity that = (ImageEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    public ImageDTO getDTO() {
        ImageDTO dto = new ImageDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }
}
