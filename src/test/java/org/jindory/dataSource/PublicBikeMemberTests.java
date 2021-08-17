package org.jindory.dataSource;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
public class PublicBikeMemberTests {
	@Autowired
	private PublicBikeMember publicBikeMember;
	
//	@Test
//	public void register() {
//		PublicBikeMemberVO member = new PublicBikeMemberVO();
//		member.setMemberActiveYn("Y");
//		member.setMemberAddress("서울시");
//		member.setMemberBerth("99991231");
//		member.setMemberGender("M");
//		member.setMemberId("yjs@naver.com");
//		member.setMemberName("아무개");
//		member.setMemberPhone("010-0000-0000");
//		member.setMemberPw("123456");
//		member.setMemberRole("따릉이");
//		publicBikeMember.register(member);
//	}
	
//	@Test
//	public void testCheckId() {
//		log.info("======================= Test CheckId function =======================");
//		String memberId = "yjs@google.com";
//		String result = publicBikeMember.checkId(memberId);
//		log.info("Result : "+result);
//		log.info("=====================================================================");
//	}
	
//	@Test
//	public void testLogin() {
//		log.info("======================= Test login function =======================");
//		PublicBikeMemberVO member = new PublicBikeMemberVO();
//		PublicBikeMemberVO rtnVal = new PublicBikeMemberVO();
//		member.setMemberId("yjs111@daum.com");
//		member.setMemberPw("******");
//		rtnVal = publicBikeMember.login(member);
//		log.info(rtnVal);
//		rtnVal.getAuthList().forEach(authVo->log.info(authVo));
//		log.info("============================== End ===============================");
//	}
	
//	@Test
//	public void testRead() {
//		log.info("======================= Test Read function =======================");
//		String member = "yjs445566@naver.com";
//		PublicBikeMemberVO returnVal = new PublicBikeMemberVO();
//		returnVal = publicBikeMember.read(member);
//		log.info(returnVal);
//		log.info("============================== End ===============================");
//	}
	
//	@Test
//	public void testInsertFavorites() {
//		PublicBikeFavoritesVO favorites = new PublicBikeFavoritesVO();
//		favorites.setMemberId("yjs445566@naver.com");
//		favorites.setStationId("ST-712");
//		favorites.setStationName("2104. 사당역 5번출구");
//		favorites.setNoticeBikeNum(0);
//		favorites.setNoticeScope(1);
//		String effectiveDate = "2021-07-29 07:29";
//		favorites.setEffectiveDate(effectiveDate);
//		favorites.setActiveYn("Y");
//		Long result = publicBikeMember.insertFavorites(favorites);
//		if(result>0) {
//			log.info("Test end successfully");
//		}else {
//			log.info("Test couldn't end successfully");
//		}
//	}
	
//	@Test
//	public void testUpdateFavorites() {
//		PublicBikeFavoritesVO favorites = new PublicBikeFavoritesVO();
//		favorites.setMemberId("yjs445566@naver.com");
//		favorites.setStationId("ST-18");
//		//favorites.setNoticeBikeNum(0);
//		String effectiveDate = "2021-07-29 07:29";
//		favorites.setEffectiveDate("2021-07-29 07:29");
//		Long result = publicBikeMember.updateFavorites(favorites);
//		if(result>0) {
//			log.info("Test end successfully");
//		}else {
//			log.info("Test couldn't end successfully");
//		}
//		log.info("============================= End =================================");
//	}
	
//	@Test
//	public void testDeleteFavorites() {
//		PublicBikeFavoritesVO favorites = new PublicBikeFavoritesVO();
//		log.info("============================= Test delete favorites =================================");
//		favorites.setMemberId("yjs445566@naver.com");
//		favorites.setStationId("ST-18");
//		Long result = publicBikeMember.deleteFavorites(favorites);
//		if(result>0) {
//			log.info("Test end successfully");
//		}else {
//			log.info("Test couldn't end successfully");
//		}
//		log.info("============================= End =================================");
//	}

//	@Test
//	public void testSearchFavorites(){
//		Criteria cri = new Criteria();
//		cri.setPageNum(1);
//		cri.setAmount(10);
//		cri.setMemberId("yjs445566@naver.com");
//		log.info("====================================== Test Search favorites ======================================");
//		List<PublicBikeFavoritesVO> favorites = new ArrayList<PublicBikeFavoritesVO>();
//		favorites = publicBikeMember.searchFavorites(cri);
//		log.info(favorites);
//		log.info("============================================== End ====================================================");
//	}
	
//	@Test
//	public void testGetFavoritesCount() {
//		log.info("============================================== Start test getFavoritesCount ====================================================");
//		int cnt = publicBikeMember.getFavoritesCount("yjs445566@naver.com");
//		log.info("Total Count : "+cnt);
//		log.info("============================================== End ====================================================");
//	}
	
	@Test
	public void testGetAlertFavorites() {
		log.info("============================================== Start test GetAlertFavorites ====================================================");
		List<PublicBikeFavoritesVO> favoritesList = new ArrayList<PublicBikeFavoritesVO>();
		favoritesList = publicBikeMember.getAlertFavorites();
		favoritesList.forEach(data -> log.info(data));
		log.info("============================================== End ====================================================");
	}
	
}
