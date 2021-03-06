package org.jindory.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jindory.api.ApiExplorer;
import org.jindory.api.PublicBikeRent;
import org.jindory.domain.AirVO;
import org.jindory.domain.BikeVO;
import org.jindory.domain.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Handles requests for the application home page.
 */

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping("/airportForm")
	public String airportForm() {
		return "airportForm";
	}
	
	@RequestMapping("/bikeForm")
	public String bikeForm() {
		return "bikeForm";
	}
	
	@RequestMapping(value="/airport",method=RequestMethod.POST)
	public String airport(AirVO airVO, Model model) throws Exception{
		Map<String,String> result = MyUtils.getAirPortID();
		String depAirportId = result.get(airVO.getDepAirportNm());
		String arrAirportId = result.get(airVO.getArrAirportNm());
		String depPlandTime = airVO.getDepPlandTime();
		String arrPlandTime = airVO.getArrPlandTime();
		
		List<AirVO> go = ApiExplorer.getAirportJson(depAirportId,arrAirportId,depPlandTime);
		List<AirVO> back = ApiExplorer.getAirportJson(depAirportId,arrAirportId,arrPlandTime);
		
		model.addAttribute("go",go);
		model.addAttribute("back",back);
		return "airport";
	}
	
}
