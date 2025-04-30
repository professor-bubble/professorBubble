package com.bubble.bubbleforprofessor.university.dto.request;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Builder
@Getter
public class UniversityApiRequest {

    private String serviceKey;

    @Builder.Default
    private int pageNo = 1;
    private int numOfRows;

    @Builder.Default
    private String dataType ="xml";

    @Builder.Default
    private String fcltyCd = "502040";

    public UniversityApiRequest(String serviceKey, int pageNo, int numOfRows, String dataType, String fcltyCd) {
        this.serviceKey = serviceKey;
        this.pageNo = pageNo;
        this.numOfRows = numOfRows;
        this.dataType = dataType;
        this.fcltyCd = fcltyCd;
    }


}
//todo pageNo는 페이수, numOfRows는 페이지에 넣을 값 갯수
// 내가 임으로 지정하기에는 xml에 있는 기존 값이 변할 수 있기에 xml에서 불러와야함
// step1 : 초기 작은 값 설정 1
// step2 : 초기에 불러온 값에서 totalCount 추출
// step3 : numOfRows에 대입 해서 데이터 요청
// step4 : DB에 저장