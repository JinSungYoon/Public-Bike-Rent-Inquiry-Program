package org.jindory.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.jindory.domain.AirVO;
import org.jindory.domain.ApiKeyVO;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ApiExplorer {
	 public static String getAirportData(String depAirportId,String arrAirportId,String depPlandTime) throws IOException {
	        
		 	System.out.println("depAiportId : "+depAirportId+"arrAirportId : "+arrAirportId+"depPlandTime : "+depPlandTime);
		 	
		 	ApiKeyVO apiKey = new ApiKeyVO();
		 	
		 	StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/DmstcFlightNvgInfoService/getFlightOpratInfoList"); /*URL*/
	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + apiKey.getAirportSearchApiKey()); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
	        urlBuilder.append("&" + URLEncoder.encode("depAirportId","UTF-8") + "=" + URLEncoder.encode(depAirportId, "UTF-8")); /*출발공항ID*/
	        urlBuilder.append("&" + URLEncoder.encode("arrAirportId","UTF-8") + "=" + URLEncoder.encode(arrAirportId, "UTF-8")); /*도착공항ID*/
	        urlBuilder.append("&" + URLEncoder.encode("depPlandTime","UTF-8") + "=" + URLEncoder.encode(depPlandTime, "UTF-8")); /*출발일*/
	        urlBuilder.append("&" + URLEncoder.encode("airlineId","UTF-8") + "=" + URLEncoder.encode("AAR", "UTF-8")); /*항공사ID*/
	        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8")+"="+URLEncoder.encode("json","UTF-8"));
	        URL url = new URL(urlBuilder.toString());
	        
	        System.out.println(urlBuilder.toString());
	        
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
	        System.out.println("Response code: " + conn.getResponseCode());
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = rd.readLine()) != null) {
	            sb.append(line);
	        }
	        rd.close();
	        conn.disconnect();
	        System.out.println("Result : "+sb.toString());
	        return sb.toString();
	    }
	 
	 public static List<AirVO> getAirportJson(String depAirportId,String arrAirportId,String depPlandTime)throws Exception{
		 String result = getAirportData(depAirportId,arrAirportId,depPlandTime);
		 // Json형태의 String을 Json으로 만들기
		 JSONParser parser = new JSONParser();
		 JSONObject jsonObj = (JSONObject) parser.parse(result);
		 // Object to JSONObject 다운 캐스팅. json처럼 생겨야지만 다운캐스팅 가능...
		 
		 //response
		 JSONObject j_response= (JSONObject) jsonObj.get("response");
		 //System.oout.pringln("j_response");
		 JSONObject j_body = (JSONObject) j_response.get("body");
		 JSONObject j_items = (JSONObject) j_body.get("items");
	     JSONArray a_item = (JSONArray)j_items.get("item");
		 
		 // 1. Gson -> 클래스로 변경 : AirVO
		 // Gson gson = new Gson();
		 // gson.fromJson(a_item.toString(),AirVo.class);
		 
		 // 2. Gson -> 배열로 변경 List<AirVO>
		 Gson gson = new Gson();
		 List<AirVO> list = gson.fromJson(a_item.toString(), new TypeToken<List<AirVO>>(){}.getType());
		 
		 return list;
		 
	 }
}
