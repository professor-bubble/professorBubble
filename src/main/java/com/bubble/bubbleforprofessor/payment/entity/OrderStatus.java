package com.bubble.bubbleforprofessor.payment.entity;

//USER의 주문 활성 및 취소 상태표시
//즉, 유저1이 주문을 넣었다가 취소할 수 있다는 가정하여 ORDER DB에서 주문을 삭제하지않고 아래 ENUM으로 수정
public enum OrderStatus {
    PENDING,        //주문 대기
    SUCCEEDED,      //주문 완료
    FAILED,         //주문 실패
    CANCELED        //주문 취소
}