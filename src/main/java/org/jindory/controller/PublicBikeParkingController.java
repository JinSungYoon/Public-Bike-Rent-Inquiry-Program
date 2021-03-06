package org.jindory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.jindory.api.PublicBikeRent;
import org.jindory.api.SearchLocationAddress;
import org.jindory.controller.PublicBikeParkingController;
import org.jindory.domain.BikeVO;
import org.jindory.domain.BreakdownAttachVO;
import org.jindory.domain.BreakdownReportVO;
import org.jindory.domain.Criteria;
import org.jindory.domain.LocationAddressVO;
import org.jindory.domain.PageDTO;
import org.jindory.domain.PublicBikeFavoritesVO;
import org.jindory.domain.UserDetailsVO;
import org.jindory.service.BreakdownReportService;
import org.jindory.service.PublicBikeMemberService;
import org.json.simple.JSONArray;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
@NoArgsConstructor
@AllArgsConstructor	// ???????????? ????????? ???????????? ??????????????? ???????????? ??????
public class PublicBikeParkingController {
	
	private static final int THREAD_POOL_SIZE = 10;
	
	private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	
	@Autowired
	private BreakdownReportService breakdownReportService;
	
	@Autowired 
	private PublicBikeMemberService publicBikeMemberService;
	
	@RequestMapping(value="/publicBikeParking",method=RequestMethod.GET)
	public String bikeRealTimeRent(Criteria cri,Model model,@AuthenticationPrincipal UserDetailsVO user,Authentication authentication) throws Exception{
		
		String memberId=null;
		
		if(authentication!= null) {
			String userName = authentication.getName();
			UserDetailsVO userInfo = (UserDetailsVO)authentication.getPrincipal();
			memberId = userInfo.getUsername();
			cri.setMemberId(memberId);
		}
			
		List<PublicBikeFavoritesVO> favoritesList = new ArrayList<PublicBikeFavoritesVO>();
		
		List<BikeVO> stationList = new ArrayList<BikeVO>();
		List<BikeVO> currentState;
		PageDTO pageInfo = null;
		
		if(memberId!=null) {
			int cnt = publicBikeMemberService.getFavoritesCount(memberId);
			pageInfo = new PageDTO(cri, cnt);
			favoritesList = publicBikeMemberService.searchFavorites(cri);
		}		
		model.addAttribute("favorites",favoritesList);
		
		model.addAttribute("pageMarker",pageInfo);
		
		return "/board/publicBikeParking";
	}
	
	//@ResponseBody??? ??????????????? view??? ????????????????????? ?????????, JSON ?????? Object ????????? ???????????? ??????
	@RequestMapping(value="/publicBikeParkingList",method=RequestMethod.POST)
	public @ResponseBody Object publicBikeParkingList() throws Exception{
		
		long start = System.currentTimeMillis(); //???????????? ?????? ??????
		
		Future<Object> future = executor.submit(()->{
			JSONArray list = new JSONArray();
			IntStream.range(0, 2).forEach(n->{
				JSONArray temp = new JSONArray();
				try {
					temp = PublicBikeRent.getbikeRentResultJson("bikeList", Integer.toString((n*1000)+1),Integer.toString((n*1000)+1000));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				list.addAll(temp);
			});
			return list;
		});
		
		JSONArray result = (JSONArray) future.get();
		
		long end = System.currentTimeMillis(); //??????????????? ????????? ?????? ??????
		System.out.println( "?????? ?????? : " + ( end - start )/1000.0 +"???"); //?????? ?????? ?????? ??? ??????
		
		return result;
	}
	
	//@ResponseBody??? ??????????????? view??? ????????????????????? ?????????, JSON ?????? Object ????????? ???????????? ??????
	// placeName??? ????????? ????????? ??????
	@RequestMapping(value="/searchPlace",method=RequestMethod.POST)	
	public @ResponseBody Object searchLocationAddress(HttpServletRequest request)throws Exception{
		
		JSONArray placeList = new JSONArray();
		
		String placeName = request.getParameter("placeName");
		
		placeList = SearchLocationAddress.getLocationAddressResultJson(placeName);
		
		return placeList;
	}
	
	// ???????????? ?????? ????????? Display
	@RequestMapping(value="/breakdownList",method=RequestMethod.GET)
	public String breakdownReport(Criteria cri,Model model)throws Exception{
		
		List<BreakdownReportVO> breakdownList = new ArrayList<BreakdownReportVO>();
		
		breakdownList = breakdownReportService.searchList(cri);
		
		int count = breakdownReportService.getTotalCount(cri);
		
		PageDTO pageInfo = new PageDTO(cri, count);
		
		model.addAttribute("breakdownReport",breakdownList);
		model.addAttribute("pageMarker",pageInfo);
		
		return "/board/breakdownList";
	}
	
	// ???????????? ???????????? ??????
	@RequestMapping(value="/getBreakdownReport",method=RequestMethod.GET)
	public String getBreakdownReport(@RequestParam("bnum") Long bnum,Model model)throws Exception{
		
		BreakdownReportVO post = new BreakdownReportVO();
		
		post = breakdownReportService.get(bnum);
		
		model.addAttribute("post",post);
		
		return "/board/detailBreakdownReport";
	}
		
	// ???????????? ????????? ???????????? ??????
	@RequestMapping(value="/registerBreakdown",method=RequestMethod.GET)
	public String showRegisterformat(Criteria cri,Model model)throws Exception{
		return "/board/registerBreakdown";
	}
	
	// ???????????? ????????? ??????(?????? ?????? ?????????????????? ????????? ???????????? ????????????)
	@RequestMapping(value="/detailBreakdownReport",method=RequestMethod.GET)
	public String showDetailBreakdownReport(Criteria cri,Model model)throws Exception{
		return "/board/detailBreakdownReport";
	}
	
	// ???????????? ????????? ??????
	@RequestMapping(value="/registerBreakdown",method=RequestMethod.POST)
	public String registerBreakdown(BreakdownReportVO data,RedirectAttributes rttr)throws Exception{
		
		log.info("Data : "+ data );
		
		Long bnum = breakdownReportService.register(data);
		
		// ????????? ?????? 1 ????????? ??????(????????? ????????? ??????????????? ???????????????)
		if(bnum>0) {
			bnum = breakdownReportService.getMaxNum();
		}
		
		// ???????????? ??????
		if(data.getAttachList() != null) {
			data.getAttachList().forEach(attach->log.info(attach));
		}
		
		rttr.addFlashAttribute("returnVal",bnum);
		
		return "redirect:/board/breakdownList";
	}
	
	// ???????????? ???????????? ??????
	// @RequestBody ?????????????????? HTTP ?????? ????????? ?????? ????????? ?????? ????????? ???????????? ????????? ??????.
	@RequestMapping(value="/updateBreakdownReport",method=RequestMethod.POST)
	public String updateBreakdownReport(@RequestBody BreakdownReportVO input,RedirectAttributes rttr)throws Exception {
				
		HashMap<String,String> returnVal = new HashMap<String,String>();
		
		Long num = breakdownReportService.modify(input);
		
		// ???????????? ??????
		if(input.getAttachList() != null) {
			input.getAttachList().forEach(attach->log.info(attach));
		}
		
		// update??? ??????????????? ?????????????????????, update?????? ?????? ??????.
		if(num>0) {
			num = input.getBnum();
		}
		
		log.info("Modify num : "+num);

		returnVal.put("event", "U");
		returnVal.put("returnVal",Long.toString(num));
		
		rttr.addFlashAttribute("returnVal",num);
		
		return "redirect:/board/breakdownList";
	}
	
	// ???????????? ???????????? ??????
	@RequestMapping(value="/deleteBreakdownReport",method=RequestMethod.POST)
	public String deleteBreakdownReport(@RequestParam("bnum") Long bnum,RedirectAttributes rttr)throws Exception {
		
		log.info("remove "+bnum);
		
		List<BreakdownAttachVO> attachList = breakdownReportService.getAttachList(bnum);
				
		Long num = breakdownReportService.delete(bnum);
		
		HashMap<String,String> returnVal = new HashMap<String,String>();
		
		// delete??? ??????????????? ?????????????????????, ???????????? ????????? ??????.
		if(num>0) {
			num = bnum;
			deletFiles(attachList);
		}
		
		log.info("Delete num : "+num);

		returnVal.put("event", "D");
		returnVal.put("returnVal",Long.toString(num));
		
		rttr.addFlashAttribute("returnVal",num);
		
		return "redirect:/board/breakdownList";
	}
	
	@GetMapping(value="/getAttachList",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<BreakdownAttachVO>> getAttachList(Long bnum){
		log.info("Get attach List "+bnum);
		return new ResponseEntity<>(breakdownReportService.getAttachList(bnum),HttpStatus.OK);
	}
	
	private void deletFiles(List<BreakdownAttachVO> attachList) {
		if(attachList == null || attachList.size() == 0) {
			return;
		}
		
		log.info("delete attach files.................... ");
		log.info(attachList);
		
		attachList.forEach(attach->{
			try {
				Path file = Paths.get("D:\\Programming\\Java\\workspace\\publicDataPortal\\upload\\"+attach.getUploadPath()+"\\"+attach.getUuid()+"_"+attach.getFileName());
				Files.deleteIfExists(file);
				if(Files.probeContentType(file).startsWith("image")) {
					Path thumbNail = Paths.get("D:\\Programming\\Java\\workspace\\publicDataPortal\\upload\\"+attach.getUploadPath()+"\\s_"+attach.getUuid()+"_"+attach.getFileName());
					Files.deleteIfExists(thumbNail);
				}
				
			}catch(Exception e) {
				log.error("delete file error" + e.getMessage());
			}// end try & catch
		}); // end forEach
	}
	
}
