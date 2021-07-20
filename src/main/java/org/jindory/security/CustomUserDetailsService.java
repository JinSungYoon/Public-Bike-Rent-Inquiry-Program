package org.jindory.security;

import java.util.ArrayList;
import java.util.List;

import org.jindory.dataSource.PublicBikeMember;
import org.jindory.domain.PublicBikeMemberVO;
import org.jindory.domain.UserDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private PublicBikeMember publicBikeMemberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//log.warn("Load user by username : "+username);
		
		// 최종적으로 리턴해야할 객체
		UserDetailsVO UserDetails = new UserDetailsVO();
		
		// 사용자 정보 select
		PublicBikeMemberVO memberInfo = publicBikeMemberMapper.read(username);
		
		if(memberInfo == null) {
			return null;
		}else {
			
			// BcryptPasswordEncoder encoding
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedPassword = passwordEncoder.encode(memberInfo.getMemberPw());
			
			UserDetails.setUsername(memberInfo.getMemberId());
			UserDetails.setPassword(encodedPassword);
			UserDetails.setMembername(memberInfo.getMemberName());
			
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			
			for(int loop=0;loop<memberInfo.getAuthList().size();loop++) {
				authorities.add(new SimpleGrantedAuthority(memberInfo.getAuthList().get(loop).getAuth()));
			}
			UserDetails.setAuthorities(authorities);
		}
		
		return UserDetails;
	}

}
