package com.bubble.bubbleforprofessor.university.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Table(name="University")
public class University {

    @Id
    @jakarta.persistence.Id
    @Column(name="university_id")
    private Long universityId;

    @Column(name="university_name", nullable = false)
    private String universityName;

    @Column(name="is_deleted", nullable = false)
    private boolean isDeleted;


    public University(Long universityId, String universityName, boolean isDeleted) {
        this.universityId = universityId;
        this.universityName = universityName;
        this.isDeleted = isDeleted;
    }

    public void updateNameIfChanged(String newName) {
        if (!this.universityName.equals(newName)) {
            this.universityName = newName;
        }
    }

    public void restoreIfDeleted() {
        if (this.isDeleted) {
            this.isDeleted = false;
        }
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

}
