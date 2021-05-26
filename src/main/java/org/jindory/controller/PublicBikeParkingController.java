package org.jindory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

import org.jindory.api.publicBikeRent;
import org.jindory.controller.PublicBikeParkingController;
import org.jindory.domain.BikeVO;
import org.jindory.domain.Criteria;
import org.jindory.domain.PageDTO;

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
				
		currentState = publicBikeRent.getbikeRentResultJson("bikeList", Integer.toString((cri.getPageNum()*10)+1),Integer.toString((cri.getPageNum()*10)+cri.getAmount()));
		result.addAll(currentState);
				
		model.addAttribute("result",result);
		model.addAttribute("pageMarker",pageInfo);
		return "/board/publicBikeParking";
	}
	
}
