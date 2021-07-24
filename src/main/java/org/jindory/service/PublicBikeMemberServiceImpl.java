package org.jindory.service;

import org.jindory.dataSource.BreakdownAttach;
import org.jindory.dataSource.BreakdownReport;
import org.jindory.dataSource.PublicBikeMember;
import org.jindory.domain.PublicBikeMemberAuthVO;
import org.jindory.domain.PublicBikeMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@AllArgsConstructor
public class PublicBikeMemberServiceImpl implements PublicBikeMemberService {

	@Autowired
	private PublicBikeMember publicBikeMember;
	
	@Override
	public Long register(PublicBikeMemberVO member) {
		PublicBikeMemberAuthVO auth = new PublicBikeMemberAuthVO();
		if(member.getMemberRole().equals("따릉이")) {
			member.setMemberRole("관리자");
			auth.setMemberId(member.getMemberId());
			auth.setAuth("ADMIN");
		}else {
			member.setMemberRole("이용자");
			auth.setMemberId(member.getMemberId());
			auth.setAuth("MEMBER");
		}
		Long resultVal = publicBikeMember.registerUserInfo(member);
		Long result = publicBikeMember.registerUserAuth(auth);
		return resultVal;
	}

	@Override
	public String checkId(String memberId) {
		String reuslt = publicBikeMember.checkId(memberId);
		return reuslt;
	}

	@Override
	public PublicBikeMemberVO login(PublicBikeMemberVO member) {
		PublicBikeMemberVO returnVal = new PublicBikeMemberVO();
		returnVal = publicBikeMember.login(member);
		return returnVal;
	}

}
