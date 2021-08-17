package org.jindory.dataSource;

import java.util.List;

import org.jindory.domain.Criteria;
import org.jindory.domain.PublicBikeFavoritesVO;
import org.jindory.domain.PublicBikeMemberAuthVO;
import org.jindory.domain.PublicBikeMemberVO;

public interface PublicBikeMember {

	public Long registerUserInfo(PublicBikeMemberVO member);
	
	public Long registerUserAuth(PublicBikeMemberAuthVO member);
	
	public PublicBikeMemberVO login(PublicBikeMemberVO member);
	
	public PublicBikeMemberVO read(String memberId);
	
	public String checkId(String memberId);
	
	public List<PublicBikeFavoritesVO> searchFavorites(Criteria cri);
	
	public int getFavoritesCount(String memberId); 
									   
	public List<PublicBikeFavoritesVO> getAlertFavorites();
	
	public Long insertFavorites(PublicBikeFavoritesVO favorites);
	
	public Long updateFavorites(PublicBikeFavoritesVO favorites);
	
	public Long deleteFavorites(PublicBikeFavoritesVO favorites);
	

}
