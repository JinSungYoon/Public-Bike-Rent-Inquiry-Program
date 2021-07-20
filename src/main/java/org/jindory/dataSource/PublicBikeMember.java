package org.jindory.dataSource;

import org.jindory.domain.PublicBikeMemberVO;

public interface PublicBikeMember {

	public Long register(PublicBikeMemberVO member);
	
	public PublicBikeMemberVO login(PublicBikeMemberVO member);
	
	public PublicBikeMemberVO read(String memberId);
	
	public String checkId(String memberId);
	
}
