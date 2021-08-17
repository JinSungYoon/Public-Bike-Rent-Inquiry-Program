package org.jindory.service;

import java.util.List;

import org.jindory.domain.Criteria;
import org.jindory.domain.PublicBikeFavoritesVO;
import org.jindory.domain.PublicBikeMemberVO;

public interface PublicBikeMemberService {
	public Long register(PublicBikeMemberVO member);
	public PublicBikeMemberVO login(PublicBikeMemberVO member);
	public String checkId(String memberId);
	public List<PublicBikeFavoritesVO> searchFavorites(Criteria cri);
	public int getFavoritesCount(String memberId);
	public List<PublicBikeFavoritesVO> getAlertFavorites();
	public Long registerFavorites(PublicBikeFavoritesVO favorites);
	public Long updateFavorites(PublicBikeFavoritesVO favorites);
	public Long deleteFavorites(PublicBikeFavoritesVO favorites);
	
}
