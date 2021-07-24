package org.jindory.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jindory.domain.PublicBikeMemberVO;
import org.jindory.service.PublicBikeMemberServiceImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/member/*")
public class PublicBikeMemberController {
	
	@Autowired
	private PublicBikeMemberServiceImpl publicBikeMemberService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@GetMapping("/registerUserView")
	public String getRegister() throws Exception{
		log.info("Get Register");
		return "/member/registerUserView";
	}
	
	@RequestMapping(value="/registerUserView", produces="text/plain;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String postRegister(@RequestBody PublicBikeMemberVO member)throws Exception{
		
		// 비밀번호 암호화
		member.setMemberPw(passwordEncoder.encode(member.getMemberPw()));
		
		Long resultVal = publicBikeMemberService.register(member);
		String returnMsg= "";
		if(resultVal.equals(1L)) {
			returnMsg = member.getMemberId();
		}else {
			returnMsg = null;
		}
		return returnMsg;
	}
	
	@RequestMapping(value="/loginView",method= {RequestMethod.GET,RequestMethod.POST})
	public String getLogin(HttpServletRequest request) {

		// 요청 시점의 사용자 URI 정보를 Session의 Attribute에 담아서 전달(잘 지워줘야 함)
		// 로그인이 틀려서 다시 하면 요청 시점의 URI가 로그인 페이지가 되므로 조건문 설정
		String uri = request.getHeader("Referer");
		if(uri ==null) {
			request.getSession().setAttribute("prevPage",
					"/board/publicBikeParking");
		}else if(!uri.contains("/loginView")) {
			request.getSession().setAttribute("prevPage",
					request.getHeader("Referer"));
		}else if(uri.contains("/registerUserView")) {
			request.getSession().setAttribute("prevPage",
					"/board/publicBikeParking");
		}
		return "/member/loginView";
	}

	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception{
		
		session.invalidate();
		return "redirect:/member/loginView";
	}
	
	@GetMapping("/mypageView")
	public void logoutGet() {
		log.info("mypageView");
	}
	
	@GetMapping("/checkId")
	@ResponseBody
	public String getCheckIdResult(@RequestParam("memberId") String memberId)throws Exception{
		
		String result = publicBikeMemberService.checkId(memberId);
		// 만일 해당 Id가 존재한다면 result갑 반환 없다면 pass 반환.
		result = result == null ? "pass" : result;
		
		return result;
	}
	
	@GetMapping("/accessError")
	public String getAccessDenied(Authentication auth,Model model) {
		log.info("access Denied "+auth);
		model.addAttribute("msg","Access Denied");
		return "/member/accessError";
	}
	
	@PostMapping("/accessError")
	public void postAccessDenied(Authentication auth,Model model) {
		log.info("access Denied "+auth);
		model.addAttribute("msg","Access Denied");
	}
	
}
