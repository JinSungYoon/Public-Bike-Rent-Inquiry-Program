package org.jindory.service;

import org.jindory.domain.PublicBikeMemberVO;

public interface PublicBikeMemberService {
	public Long register(PublicBikeMemberVO member);
	public PublicBikeMemberVO login(PublicBikeMemberVO member);
	public String checkId(String memberId);
}
