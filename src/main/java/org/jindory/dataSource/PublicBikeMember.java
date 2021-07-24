package org.jindory.dataSource;

import org.jindory.domain.PublicBikeMemberAuthVO;
import org.jindory.domain.PublicBikeMemberVO;

public interface PublicBikeMember {

	public Long registerUserInfo(PublicBikeMemberVO member);
	
	public Long registerUserAuth(PublicBikeMemberAuthVO member);
	
	public PublicBikeMemberVO login(PublicBikeMemberVO member);
	
	public PublicBikeMemberVO read(String memberId);
	
	public String checkId(String memberId);

}
