package com.bubble.bubbleforprofessor.university.dto.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@Builder
public class UniversityList {
    @XmlElement(name = "OBJT_ID")
    private Long objectId;

    @XmlElement(name = "FCLTY_NM")
    private String universityName;

    public UniversityList(Long objectId, String universityName) {
        this.objectId = objectId;
        this.universityName = universityName;
    }
}