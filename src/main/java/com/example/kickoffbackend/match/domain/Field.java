package com.example.kickoffbackend.match.domain;

import com.example.kickoffserver.dto.match.request.MatchRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString(of = {"id"})
@Table(name = "fields")
public class Field {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Long id;

    @Column
    private String fieldName;

    @Column
    private String address;

    @Column
    private int imageAttached; // 1 or 0

    @OneToOne(mappedBy = "field") // 무조건 걸어 두지 말고 나중에 많이 쓰이는 곳에서
    private Match match;

    @OneToMany(mappedBy = "field", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FieldImage> images = new ArrayList<>();

    public static Field createField(MatchRequest request) {
        return Field.builder()
                .fieldName(request.getFieldName())
                .address(request.getAddress())
                .imageAttached(0)
                .build();
    }

    public static Field createImagesField(MatchRequest request) {
        return Field.builder()
                .fieldName(request.getFieldName())
                .address(request.getAddress())
                .imageAttached(1)
                .build();
    }

    public static Field updateField(Field field, String fieldName, String address) {
        return field.toBuilder()
                .fieldName(fieldName)
                .address(address) // TODO : 저장 되어 있는 images 넣어 줘야 함
                .build();
    }

}