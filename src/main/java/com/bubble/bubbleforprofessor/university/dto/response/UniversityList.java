package com.bubble.bubbleforprofessor.university.dto.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class UniversityList {
    @XmlElement(name = "OBJT_ID")
    private Long objectId;

    @XmlElement(name = "FCLTY_NM")
    private String universityName;
}
