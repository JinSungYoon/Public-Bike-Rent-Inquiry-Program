package org.jindory.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.jindory.dataSource.BreakdownAttach;
import org.jindory.dataSource.BreakdownReport;
import org.jindory.dataSource.PublicBikeMember;
import org.jindory.domain.Criteria;
import org.jindory.domain.PublicBikeFavoritesVO;
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

	@Override
	public Long registerFavorites(PublicBikeFavoritesVO favorites) {
		
		log.info("Confirm favorites info");
		log.info(favorites);
		
		// 화면단에서 YYYY-MM-DDThh:mm 형식이므로 YYYY-MM-DD hh:mm으로 변환 
		if(favorites.getEffectiveDate()!=null) {
			favorites.setEffectiveDate(favorites.getEffectiveDate().replace("T"," "));
		}
		
		Long result = publicBikeMember.insertFavorites(favorites);
		
		return result;
	}
	
	@Override
	public Long updateFavorites(PublicBikeFavoritesVO favorites) {
		
		// 화면단에서 YYYY-MM-DDThh:mm 형식이므로 YYYY-MM-DD hh:mm으로 변환 
		favorites.setEffectiveDate(favorites.getEffectiveDate().replace("T"," "));
		
		Long result = publicBikeMember.updateFavorites(favorites);
		
		return result;
	}

	@Override
	public Long deleteFavorites(PublicBikeFavoritesVO favorites) {
		
		log.info("Delete favorites information");
		
		Long result = publicBikeMember.deleteFavorites(favorites);
		
		return result;
	}

	@Override
	public List<PublicBikeFavoritesVO> searchFavorites(Criteria cri) {
		List<PublicBikeFavoritesVO> favoritesList= new ArrayList<PublicBikeFavoritesVO>();
		List<PublicBikeFavoritesVO> returnList= new ArrayList<PublicBikeFavoritesVO>();
		PublicBikeFavoritesVO favorite = new PublicBikeFavoritesVO();
		favoritesList = publicBikeMember.searchFavorites(cri);
		
		// 화면단에서 YYYY-MM-DDThh:mm형식이므로 YYYY-MM-DD hh:mm를 YYYY-MM-DDThh:mm으로 변환
		for(int idx=0;idx<favoritesList.size();idx++) {
			// null check
			if(favoritesList.get(idx).getEffectiveDate()!=null) {
				favoritesList.get(idx).setEffectiveDate(favoritesList.get(idx).getEffectiveDate().replace(" ","T"));
			}
		} 
		
		return favoritesList;
	}

	@Override
	public int getFavoritesCount(String memberId) {
		int cnt = publicBikeMember.getFavoritesCount(memberId);
		return cnt;
	}

	@Override
	public List<PublicBikeFavoritesVO> getAlertFavorites() {
		List<PublicBikeFavoritesVO> alertFavoritesList= new ArrayList<PublicBikeFavoritesVO>();
		alertFavoritesList = publicBikeMember.getAlertFavorites();
		return alertFavoritesList;
	}


}
