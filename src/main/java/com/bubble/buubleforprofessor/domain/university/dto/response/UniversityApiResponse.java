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
@XmlAccessorType(XmlAccessType.FIELD) //모든 필드에 접근하도록 설정
/*
* @XmlAccessorType는 JAXB가 XML 데이터를 Java 객체에 넣을 때 어디를 통해 접근할지 정하는 규칙
기본값: 만약 아무것도 안 쓰면, JAXB는 public 필드나 public getter/setter만 확인함 private은 무시
XmlAccessType.FIELD: 모든 필드(심지어 private도 포함)를 직접 접근해서 데이터를 넣어줘, getter/setter가 없어도 필드 자체를 사용할 수 있게 해줌
* */
@NoArgsConstructor
public class UniversityApiResponse {
    @XmlElement(name = "header")
    private Header header;

    @XmlElement(name = "body")
    private Body body;
}
