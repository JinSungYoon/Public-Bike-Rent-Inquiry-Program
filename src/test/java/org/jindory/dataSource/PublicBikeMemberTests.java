package org.jindory.dataSource;

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
	
	@Test
	public void testRead() {
		log.info("======================= Test Read function =======================");
		String member = "yjs@naver.com";
		PublicBikeMemberVO returnVal = new PublicBikeMemberVO();
		returnVal = publicBikeMember.read(member);
		log.info(returnVal);
		log.info("============================== End ===============================");
	}
}
