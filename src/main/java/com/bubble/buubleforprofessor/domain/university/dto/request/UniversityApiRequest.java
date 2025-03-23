package com.bubble.buubleforprofessor.domain.university.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniversityApiRequest {
    private String serviceKey = "W9J4P7PW-W9J4-W9J4-W9J4-W9J4P7PW4Y";
    private int pageNo = 1;
    private int numOfRows;
    private String dataType ="xml";
    private String fcltyCd = "502040";
}
//todo pageNo는 페이수, numOfRows는 페이지에 넣을 값 갯수
// 내가 임으로 지정하기에는 xml에 있는 기존 값이 변할 수 있기에 xml에서 불러와야함
// step1 : 초기 작은 값 설정 1
// step2 : 초기에 불러온 값에서 totalCount 추출
// step3 : numOfRows에 대입 해서 데이터 요청
// step4 : DB에 저장
