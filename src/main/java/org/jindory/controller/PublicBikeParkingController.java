package org.jindory.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jindory.api.PublicBikeRent;
import org.jindory.api.SearchLocationAddress;
import org.jindory.controller.PublicBikeParkingController;
import org.jindory.domain.BikeVO;
import org.jindory.domain.Criteria;
import org.jindory.domain.LocationAddressVO;
import org.jindory.domain.PageDTO;
import org.json.simple.JSONArray;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor	// 생성자를 만들고 자동으로 주입하도록 처리하기 위해
public class PublicBikeParkingController {

	@RequestMapping(value="/publicBikeParking",method=RequestMethod.GET)
	public String bikeRealTimeRent(Criteria cri,Model model) throws Exception{
		
		List<BikeVO> result = new ArrayList<BikeVO>();
		List<BikeVO> currentState;
		
		PageDTO pageInfo = new PageDTO(cri, 2000);
				
		currentState = PublicBikeRent.getbikeRentResultList("bikeList", Integer.toString((cri.getPageNum()*10)+1),Integer.toString((cri.getPageNum()*10)+cri.getAmount()));
		result.addAll(currentState);

		model.addAttribute("result",result);
		model.addAttribute("pageMarker",pageInfo);
		return "/board/publicBikeParking";
	}
	
	//@ResponseBody를 사용해주면 view를 생성해주는것이 아니라, JSON 혹은 Object 형태로 데이터로 전달
	@RequestMapping(value="/publicBikeParkingList",method=RequestMethod.POST)
	public @ResponseBody Object publicBikeParkingList() throws Exception{
		
		JSONArray list = new JSONArray();
		list = PublicBikeRent.getbikeRentResultJson("bikeList", "1","1000");
						
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

}
