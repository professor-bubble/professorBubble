package com.bubble.bubbleforprofessor.university.dto.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import lombok.*;

import java.util.List;


@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@Builder
public class Body {
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<UniversityList> items;

    @XmlElement(name = "numOfRows")
    private int numOfRows;

    @XmlElement(name = "pageNo")
    private int pageNo;

    @XmlElement(name = "totalCount")
    private int totalCount;

    public Body(List<UniversityList> items, int numOfRows, int pageNo, int totalCount) {
        this.items = items;
        this.numOfRows = numOfRows;
        this.pageNo = pageNo;
        this.totalCount = totalCount;
    }
}