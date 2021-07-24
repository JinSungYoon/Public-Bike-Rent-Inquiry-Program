package org.jindory.security;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class CustomUserLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		
		// IP, 세션 ID
		WebAuthenticationDetails web= (WebAuthenticationDetails) authentication.getDetails();
		System.out.println("IP : "+web.getRemoteAddress());
		System.out.println("Session ID : "+web.getSessionId());
		
		// 인증 ID
		System.out.println("name : "+authentication.getName());
		
		// 권한 리스트
		List<GrantedAuthority> authList = (List<GrantedAuthority>) authentication.getAuthorities();
		System.out.println("권한 : ");
		for(int loop=0;loop<authList.size();loop++) {
			System.out.println(authList.get(loop).getAuthority() + " ");
		}		
		System.out.println();
				
		// 세션 Attribute 확인
		Enumeration<String> list = request.getSession().getAttributeNames();
		while(list.hasMoreElements()) {
			System.out.println(list.nextElement());
		}
		
		// 디폴트 uri
		String uri = "/";
		
		// Security가 요청을 가로챈 경우 사용자가 원래 요청했던 URI 정보를 저장한 객체
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		
		/* 로그인 버튼 눌러 접속했을 경우의 데이터 get */ 
		String prevPage = (String) request.getSession().getAttribute("prevPage");
		
		if(prevPage != null) {
			request.getSession().removeAttribute("prevPage");
		}
		
		// null이 아니라면 강제 인터셉트 당했다는 것
		if(savedRequest != null) {
			uri = savedRequest.getRedirectUrl();
			
			// 세션에 저장된 객체를 다 사용한 뒤에는 지워줘서 메모리 누수 방지.
			requestCache.removeRequest(request, response);
			
			System.out.println(uri);
		// ""가 아니라면 직접 로그인 페이지로 접속한것.
		}else if(prevPage != null && !prevPage.contentEquals("")) {
			uri = prevPage;
		}
		
		response.sendRedirect(uri);
		
	}
	
}
