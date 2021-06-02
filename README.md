# Public Bike Rent Inquiry Program

이 프로젝트는 서울시 공공자전거 대여 현황을 조회 및 문의사항을 등록 및 조회하는 프로그램입니다.

현재 구성한 Layout은 아래 Screenshot과 같으며 현재는 서울시 공공자전거 대여소의 현황을 조회 할 수 있습니다.
![publicBikeRentInquiryLayout](https://github.com/JinSungYoon/Public-Bike-Rent-Inquiry-Program/blob/master/img/publicBikeRentInquiryLayout.jpg)

현재 구현된 기능은 서울시 공공데이터 현황을 조회하여 조회된 자전거 대여소의 위치를 지도에 보여주고 있습니다.

- 사용된 API
  - 서울시 공공공공자전거 실시간 대여정보(http://data.seoul.go.kr/dataList/OA-15493/A/1/datasetView.do)
  - 네이버 지도 API(https://www.ncloud.com/product/applicationService/maps)	

현재 구현된 기능

- 서울시 자전거 대여소 위치 정보 지도에 표시
- 검색을 통한 위치 이동 및 가까운 대여소 조회 

향후 추가 예정 기능

- 현 위치를 기반으로 한 가장 가까운 자전거 대여소 조회
- 대여소 선택시 게시글 등록 및 수정