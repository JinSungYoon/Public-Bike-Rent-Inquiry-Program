package org.jindory.service;

import org.jindory.dataSource.BreakdownAttach;
import org.jindory.dataSource.BreakdownReport;
import org.jindory.dataSource.PublicBikeMember;
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
		Long resultVal = publicBikeMember.register(member);
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
