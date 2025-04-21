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
public class Header {
    @XmlElement(name = "resultCode")
    private String resultCode;

    @XmlElement(name = "resultMsg")
    private String resultMsg;
}
