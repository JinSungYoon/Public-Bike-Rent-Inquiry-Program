package org.jindory.security;

import org.jindory.domain.UserDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomUserLoginAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	// DB의 값을 가져다주는 커스터마이징 클래스
	UserDetailsService userDetailsService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		// 사용자가 입력한 정보 
		String memberId = authentication.getName();
		String memberPw = (String) authentication.getCredentials();
		
		// DB에서 가져온 사용자 정보
		UserDetailsVO userDetails = (UserDetailsVO) userDetailsService.loadUserByUsername(memberId);
		
		// 인증 진행
		
		// ID 및 PW 체크해서 안 맞을 경우
		if(userDetails == null || !memberId.equals(userDetails.getUsername()) || !passwordEncoder.matches(memberPw, userDetails.getPassword())) {
			throw new BadCredentialsException(memberId);
		}else if(!userDetails.isAccountNonLocked()) {	// 잠긴 계정일 경우
			throw new LockedException(memberId);
		}else if(!userDetails.isEnabled()) {			// 비활성화 된 계정일 경우 
			throw new DisabledException(memberId);
		}else if(!userDetails.isCredentialsNonExpired()) { // 만료된 계정일 경우
			throw new AccountExpiredException(memberId);
		}else if(!userDetails.isCredentialsNonExpired()) { // 비밀번호가 만료된 경우
			throw new CredentialsExpiredException(memberId);
		}
		
		// User 정보를 다 사용했을경우 Password 정보는 제거
		userDetails.setPassword(null);

		Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
		
		return newAuth;
	}

	@Override
	public boolean supports(Class<?> authentication) {

		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
