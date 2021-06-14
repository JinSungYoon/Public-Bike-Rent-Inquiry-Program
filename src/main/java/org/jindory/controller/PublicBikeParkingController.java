package org.jindory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;


import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jindory.api.PublicBikeRent;
import org.jindory.api.SearchLocationAddress;
import org.jindory.controller.PublicBikeParkingController;
import org.jindory.domain.BikeVO;
import org.jindory.domain.BreakdownReportVO;
import org.jindory.domain.Criteria;
import org.jindory.domain.LocationAddressVO;
import org.jindory.domain.PageDTO;
import org.jindory.service.BreakdownReportService;
import org.json.simple.JSONArray;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor	// 생성자를 만들고 자동으로 주입하도록 처리하기 위해
public class PublicBikeParkingController {

	@Autowired
	private BreakdownReportService breakdownReportService;
	
	@RequestMapping(value="/publicBikeParking",method=RequestMethod.GET)
	public String bikeRealTimeRent(Criteria cri,Model model) throws Exception{
		
		List<BikeVO> result = new ArrayList<BikeVO>();
		List<BikeVO> currentState;
		List<BreakdownReportVO> breakdownList = new ArrayList<BreakdownReportVO>();
		List<Map<String,Integer>> breakdownCount = new ArrayList<Map<String,Integer>>();
		
		PageDTO pageInfo = new PageDTO(cri, 2000);
				
		currentState = PublicBikeRent.getbikeRentResultList("bikeList", Integer.toString((cri.getAmount()*cri.getPageNum())-9),Integer.toString((cri.getAmount()*cri.getPageNum())));
		result.addAll(currentState);
		
		List<String> stationList = new ArrayList<String>();
		//breakdownList = breakdownReportService.searchList();
		breakdownCount = breakdownReportService.searchBreakdownCount(cri);
		
		model.addAttribute("result",result);
		model.addAttribute("pageMarker",pageInfo);
		model.addAttribute("breakdownReport",breakdownList);
		model.addAttribute("breakdownCount",breakdownCount);
		return "/board/publicBikeParking";
	}
	
	//@ResponseBody를 사용해주면 view를 생성해주는것이 아니라, JSON 혹은 Object 형태로 데이터로 전달
	@RequestMapping(value="/publicBikeParkingList",method=RequestMethod.POST)
	public @ResponseBody Object publicBikeParkingList() throws Exception{
		
		JSONArray list = new JSONArray();
		JSONArray temp = new JSONArray();
		for(int loop=1;loop<=2000;loop=loop+1000) {
			temp = PublicBikeRent.getbikeRentResultJson("bikeList", Integer.toString(loop),Integer.toString(loop+999));
			list.addAll(temp);
		}
						
		return list;
	}
	
	//@ResponseBody를 사용해주면 view를 생성해주는것이 아니라, JSON 혹은 Object 형태로 데이터로 전달
	@RequestMapping(value="/searchPlace",method=RequestMethod.POST)	
	public @ResponseBody Object searchLocationAddress(HttpServletRequest request)throws Exception{
		
		JSONArray placeList = new JSONArray();
		
		String placeName = request.getParameter("placeName");
		
		placeList = SearchLocationAddress.getLocationAddressResultJson(placeName);
		
		return placeList;
	}
	
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
	
	@RequestMapping(value="/getBreakdownReport",method=RequestMethod.GET)
	public String getBreakdownReport(@RequestParam("bnum") Long bnum,Model model)throws Exception{
		
		BreakdownReportVO post = new BreakdownReportVO();
		
		post = breakdownReportService.get(bnum);
		
		model.addAttribute("post",post);
		
		return "/board/detailBreakdownReport";
	}
		
	@RequestMapping(value="/registerBreakdown",method=RequestMethod.GET)
	public String showregisterformat(Criteria cri,Model model)throws Exception{
		return "/board/registerBreakdown";
	}
	
	@RequestMapping(value="/registerBreakdown",method=RequestMethod.POST)
	public String registerBreakdown(BreakdownReportVO data,RedirectAttributes rttr)throws Exception{
		log.info("Data : "+ data );
		
		Long bnum = breakdownReportService.register(data);
		
		log.info("BNUM : "+bnum);
				
		rttr.addFlashAttribute("result",bnum);
		
		return "redirect:/board/breakdownList";
	}

	@RequestMapping(value="/updateBreakdownReport",method=RequestMethod.POST)
	public String updateBreakdownReport(@RequestBody Map<String,String> data,RedirectAttributes rttr)throws Exception {
		
		BreakdownReportVO input = new BreakdownReportVO();
		
		input.setBnum(Long.parseLong(data.get("bnum")));
		input.setBikenum(Long.parseLong(data.get("bikenum")));
		input.setBrokenparts(data.get("brokenparts"));
		input.setBtitle(data.get("btitle"));
		input.setContent(data.get("content"));
		input.setStationid(data.get("stationid"));
		input.setWriter(data.get("writer"));
		
		Long num = breakdownReportService.modify(input);
		
		log.info("Modify num : "+num);

		rttr.addFlashAttribute("modifyNum", num);
		
		return "redirect:/board/breakdownList";
	}
	
	@RequestMapping(value="/deleteBreakdownReport",method=RequestMethod.POST)
	public String registerBreakdownReport(@RequestParam("bnum") Long bnum,RedirectAttributes rttr)throws Exception {
		
		Long num = breakdownReportService.delete(bnum);
		
		log.info("Delete num : "+num);

		rttr.addAttribute("deleteNum", num);
		
		return "redirect:/board/breakdownList";
	}
	
}
