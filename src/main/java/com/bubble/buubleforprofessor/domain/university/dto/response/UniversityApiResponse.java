package com.bubble.buubleforprofessor.domain.university.dto.response;

/*
 * XML의 최상위 태그인 <response>를 JAXB에 알려주기 위해 설정
 * @XmlRootElement은 XML의 루트 요소가 response임을 지정하며, 이 클래스를 XML 구조의 큰 상자로 정의
 * JAXB는 XML 데이터를 이 클래스에 매핑하여 역직렬화
 * XML 구조와 Java 객체를 정확히 매핑하여 데이터를 올바르게 처리
 * WebClient의 Jaxb2XmlDecoder가 XML을 이 객체로 자동 변환
 */

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "response")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class UniversityApiResponse {
    @XmlElement(name = "header")
    private Header header;

    @XmlElement(name = "body")
    private Body body;
}
