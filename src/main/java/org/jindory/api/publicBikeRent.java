package org.jindory.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jindory.domain.ApiKeyVO;
import org.jindory.domain.BikeVO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PublicBikeRent {
	public static String getBikeRealTimeRentInfo(String serviceNm,String startIndex,String endIndex) throws IOException {
        
		ApiKeyVO apiKey = new ApiKeyVO();
		
		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/" + apiKey.getPublicBikeRealTimeRentApiKey()); /*Service Key*/
        urlBuilder.append("/" + "json"); /*type*/
        urlBuilder.append("/" + URLEncoder.encode(serviceNm,"UTF-8")); /*함수명*/
        urlBuilder.append("/" + URLEncoder.encode(startIndex, "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("/" + URLEncoder.encode(endIndex, "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        URL url = new URL(urlBuilder.toString());
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
        //System.out.println(sb.toString());
        
        return sb.toString();
    }
	
	public static List<BikeVO> getbikeRentResultList(String serviceNm,String startIndex,String endIndex)throws Exception{
		 
		System.out.println("ServiceName : "+serviceNm+" "+"Start Index : " + startIndex + " "+"End Index : "+endIndex);
		
		 String result = getBikeRealTimeRentInfo(serviceNm,startIndex,endIndex);
		 // Json형태의 String을 Json으로 만들기
		 JSONParser parser = new JSONParser();
		 JSONObject jsonObj = (JSONObject) parser.parse(result);
		 
		 //response
		 JSONObject j_rentBikeStatus= (JSONObject) jsonObj.get("rentBikeStatus");
		 System.out.println("j_rentBikeStatus : "+j_rentBikeStatus);
		 JSONArray j_row = (JSONArray) j_rentBikeStatus.get("row");
		 
		 // 2. Gson -> 배열로 변경 List<BikeVO>
		 Gson gson = new Gson();
		 List<BikeVO> list = gson.fromJson(j_row.toString(), new TypeToken<List<BikeVO>>(){}.getType());
		
		// 정류장ID로 오름차수 청렬
		Collections.sort(list, new Comparator<BikeVO>(){
			@Override
			public int compare(BikeVO b1,BikeVO b2) {
				int prev = Integer.parseInt(b1.getStationId().substring(3,b1.getStationId().length()));
				int next = Integer.parseInt(b2.getStationId().substring(3,b2.getStationId().length()));
				if(prev<next) {
					return -1;
				}else if(prev>next) {
					return 1;
				}
				return 0;
			}
		});
		 
		 return list;
		 
	 }
	
	public static JSONArray getbikeRentResultJson(String serviceNm,String startIndex,String endIndex)throws Exception{
		 
		System.out.println("ServiceName : "+serviceNm+" "+"Start Index : " + startIndex + " "+"End Index : "+endIndex);
		
		 String result = getBikeRealTimeRentInfo(serviceNm,startIndex,endIndex);
		 // Json형태의 String을 Json으로 만들기
		 JSONParser parser = new JSONParser();
		 JSONObject jsonObj = (JSONObject) parser.parse(result);
		 
		 //response
		 JSONObject j_rentBikeStatus= (JSONObject) jsonObj.get("rentBikeStatus");
		 JSONArray j_row = (JSONArray) j_rentBikeStatus.get("row");
		 //System.out.println("j_row : "+j_row);
		 
		 return j_row;
		 
	 }
	
}
