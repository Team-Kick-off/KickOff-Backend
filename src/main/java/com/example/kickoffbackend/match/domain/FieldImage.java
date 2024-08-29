package com.example.kickoffbackend.match.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString(of = {"id"})
@Table(name = "field_images")
public class FieldImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private Field field;

    public static FieldImage createFieldImages(Field field, String originalFileName, String storedFileName) {
        return FieldImage.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .field(field)
                .build();
    }

//    public void changeField(Field field) {
//        this.field = field;
//        field.getImages().add(this);
//    }

//    public void updateMatch(Match match) {
//        this.match = match;
//    }
}