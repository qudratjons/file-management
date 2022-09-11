package uz.file.management.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "file_db")
public class FileDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Lob
    @Column(name = "file_byte", length = 1000)
    private byte[] fileInByte;
}
