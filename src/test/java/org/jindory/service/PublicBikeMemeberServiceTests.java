package org.jindory.service;

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
	
	@Test
	public void testLogin() {
		log.info("=========================== Start Test Login ===========================");
		PublicBikeMemberVO member = new PublicBikeMemberVO();
		PublicBikeMemberVO returnVal = new PublicBikeMemberVO();
		member.setMemberId("yjs@google.com");
		member.setMemberPw("******");
		returnVal = publicBikeMemberService.login(member);
		//log.info("Return Val : "+returnVal);
		log.info(returnVal);
		log.info("=========================== End Test Login ===========================");
	}
	
//	
//	@Test
//	public void testCheckId() {
//		log.info("=========================== Test PublicBikemeberService ===========================");
//		String memberId = "yjs@naver.com";
//		String result = publicBikeMemberService.checkId(memberId);
//		log.info("Result : "+result);
//		log.info("===================================================================================");
//	}

}
