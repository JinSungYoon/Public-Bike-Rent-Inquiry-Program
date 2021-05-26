package org.jindory.domain;

import lombok.Data;

@Data
public class BikeVO {
	String listTotalCount;	// 총 데이터 건수(정상조회 시 출력됨)
	String resultCode;		// 요청결과 코드(하단 메세지설명 참고)
	String resultMessage;	// 요청결과 메세지(하단 메세지 설명 참고)
	String rackTotCnt;		// 거치대수
	String stationName;		// 대여소이름
	String parkingBikeTotCnt;// 자전거주차총 건수
	String shared;			// 거치율
	String stationLatitude;	// 위도
	String stationLongitude;// 경도
	String stationId;		// 대여소ID
}
