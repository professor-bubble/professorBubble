package com.bubble.buubleforprofessor.domain.university.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Document(indexName = "universities") //indexName에 대문자 들어가면안됨, Elasticsearch는 HTTP 기반 API를 사용하는데, URL 경로에서 대소문자 문제를 방지하려고 소문자만 허용
@Table(name="University")
@Builder
public class University {

    @Id
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="university_id")
    private int universityId;

    @Column(name="university_name", nullable = false)
    private String universityName;
}
