package org.jindory.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jindory.domain.Criteria;
import org.jindory.domain.PublicBikeFavoritesVO;
import org.jindory.domain.PublicBikeMemberVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Log4j
public class PublicBikeMemeberServiceTests {

	@Autowired
	private PublicBikeMemberService publicBikeMemberService;
	
//	@Test
//	public void testRegister() {
//		log.info("=========================== Test PublicBikemeberService ===========================");
//		PublicBikeMemberVO member = new PublicBikeMemberVO();
//		member.setMemberAddress("경기도 군포시");
//		member.setMemberBerth("99991231");
//		member.setMemberGender("M");
//		member.setMemberId("yjs@google.com");
//		member.setMemberName("윤진성");
//		member.setMemberPhone("010-0000-0000");
//		member.setMemberPw("*****");
//		member.setMemberRole("관리자");
//		Long returnVal = publicBikeMemberService.register(member);
//		log.info("Return val : "+returnVal);
//		log.info("=========================== End PublicBikemeberService ===========================");
//	}
	
//	@Test
//	public void testLogin() {
//		log.info("=========================== Start Test Login ===========================");
//		PublicBikeMemberVO member = new PublicBikeMemberVO();
//		PublicBikeMemberVO returnVal = new PublicBikeMemberVO();
//		member.setMemberId("yjs@google.com");
//		member.setMemberPw("******");
//		returnVal = publicBikeMemberService.login(member);
//		//log.info("Return Val : "+returnVal);
//		log.info(returnVal);
//		log.info("=========================== End Test Login ===========================");
//	}
	
//	
//	@Test
//	public void testCheckId() {
//		log.info("=========================== Test PublicBikemeberService ===========================");
//		String memberId = "yjs@naver.com";
//		String result = publicBikeMemberService.checkId(memberId);
//		log.info("Result : "+result);
//		log.info("===================================================================================");
//	}
	
//	@Test
//	public void testRegisterFavorites() {
//		log.info("=========================== Test Register Favorites ===========================");
//		PublicBikeFavoritesVO favorites = new PublicBikeFavoritesVO();
//		favorites.setMemberId("yjs445566@naver.com");
//		favorites.setStationId("ST-18");
//		favorites.setStationName("113. 홍대입구영 2번출구 앞");
//		favorites.setNoticeBikeNum(3);
//		favorites.setNoticeScope(1);
//		Date effectiveDate = new Date();
//		favorites.setEffectiveDate(effectiveDate);
//		favorites.setActiveYn("Y");
//		PublicBikeNoticeTimeVO noticeTime1 = new PublicBikeNoticeTimeVO();
//		PublicBikeNoticeTimeVO noticeTime2 = new PublicBikeNoticeTimeVO();
//		PublicBikeNoticeTimeVO noticeTime3 = new PublicBikeNoticeTimeVO();
//		List<PublicBikeNoticeTimeVO> noticeTimeList= new ArrayList<PublicBikeNoticeTimeVO>();
//		Long sum = 0L;
//		noticeTime1.setMemberId("yjs445566@naver.com");
//		noticeTime1.setStationId("ST-18");
//		noticeTime1.setStationName("113. 홍대입구영 2번출구 앞");
//		noticeTime1.setNoticeTime("15:00");
//		noticeTimeList.add(noticeTime1);
//		noticeTime2.setMemberId("yjs445566@naver.com");
//		noticeTime2.setStationId("ST-18");
//		noticeTime2.setStationName("113. 홍대입구영 2번출구 앞");
//		noticeTime2.setNoticeTime("18:00");
//		noticeTimeList.add(noticeTime2);
//		noticeTime3.setMemberId("yjs445566@naver.com");
//		noticeTime3.setStationId("ST-18");
//		noticeTime3.setStationName("113. 홍대입구영 2번출구 앞");
//		noticeTime3.setNoticeTime("19:00");
//		noticeTimeList.add(noticeTime3);
//		favorites.setNoticeTimeList(noticeTimeList);
//		
//		Long result = publicBikeMemberService.registerFavorites(favorites);
//		if(result>0) {
//			log.info("Register Favorites information successfully");
//		}else{
//			log.info("Register Favorites information failed");
//		}
//		log.info("===================================================================================");
//	}
	
//	@Test
//	public void testUpdateFavorites() {
//		log.info("=========================== Test Update Favorites ===========================");
//		PublicBikeFavoritesVO favorites = new PublicBikeFavoritesVO();
//		PublicBikeNoticeTimeVO noticeTime = new PublicBikeNoticeTimeVO();
//		List<PublicBikeNoticeTimeVO> noticeTimeList= new ArrayList<PublicBikeNoticeTimeVO>();
//		favorites.setMemberId("yjs445566@naver.com");
//		favorites.setStationId("ST-18");
//		favorites.setNoticeBikeNum(7);
//		favorites.setActiveYn("N");
//		noticeTime.setMemberId(favorites.getMemberId());
//		noticeTime.setStationId(favorites.getStationId());
//		noticeTime.setNoticeTime("08:15");
//		noticeTimeList.add(noticeTime);
//		favorites.setNoticeTimeList(noticeTimeList);
//		Long result = publicBikeMemberService.updateFavorites(favorites);
//		if(result>0) {
//			log.info("Register Favorites information successfully");
//		}else{
//			log.info("Register Favorites information failed");
//		}
//		log.info("===================================================================================");
//	}
	
//	@Test
//	public void testDeleteFavorites() {
//		log.info("=========================== Test Update Favorites ===========================");
//		PublicBikeFavoritesVO favorites = new PublicBikeFavoritesVO();
//		PublicBikeNoticeTimeVO noticeTime = new PublicBikeNoticeTimeVO();
//		List<PublicBikeNoticeTimeVO> noticeTimeList= new ArrayList<PublicBikeNoticeTimeVO>();
//		favorites.setMemberId("yjs445566@naver.com");
//		favorites.setStationId("ST-18");
//		noticeTime.setMemberId(favorites.getMemberId());
//		noticeTime.setStationId(favorites.getStationId());
//		noticeTimeList.add(noticeTime);
//		favorites.setNoticeTimeList(noticeTimeList);
//		Long result = publicBikeMemberService.deleteFavorites(favorites);
//		if(result>0) {
//			log.info("Register Favorites information successfully");
//		}else{
//			log.info("Register Favorites information failed");
//		}
//		log.info("===================================================================================");
//	}
	
//	@Test
//	public void testSearchFavorites() {
//		log.info("=========================== Test Search Favorites ===========================");
//		List<PublicBikeFavoritesVO> favoritesList = new ArrayList<PublicBikeFavoritesVO>();
//		Criteria cri = new Criteria();
//		cri.setAmount(10);
//		cri.setPageNum(1);
//		cri.setMemberId("yjs445566@naver.com");
//		favoritesList = publicBikeMemberService.searchFavorites(cri);
//		for(int index=0;index<favoritesList.size();index++) {
//			log.info(favoritesList.get(index));
//		}		
//		log.info("=============================================================================");
//	}
	
	@Test 
	public void testGetAlertFavorites() {
		log.info("=========================== Test Get Alert Favorites ===========================");
		List<PublicBikeFavoritesVO> favoritesList = new ArrayList<PublicBikeFavoritesVO>();
		favoritesList = publicBikeMemberService.getAlertFavorites();
		favoritesList.forEach(data -> log.info(data));
		log.info("=============================================================================");
	}
}
