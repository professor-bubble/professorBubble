package com.bubble.bubbleforprofessor.university.document;

import com.bubble.bubbleforprofessor.university.entity.University;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "universities") //indexName에 대문자 들어가면안됨, Elasticsearch는 HTTP 기반 API를 사용하는데, URL 경로에서 대소문자 문제를 방지하려고 소문자만 허용
public class UniversityDocument {

    @Id
    private Long universityId;

    private String universityName;

    private boolean isDeleted;


    @Builder
    private UniversityDocument(Long universityId, String universityName, boolean isDeleted) {
        this.universityId = universityId;
        this.universityName = universityName;
        this.isDeleted = isDeleted;
    }

    public static UniversityDocument fromEntity(University university) {
        return UniversityDocument.builder()
                .universityId(university.getUniversityId())
                .universityName(university.getUniversityName())
                .isDeleted(university.isDeleted())
                .build();
    }
}
