package org.jindory.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jindory.domain.ApiKeyVO;

public class samplePublicBicycle {
	
	
	
	public static void main(String[] args) throws IOException {
    
		ApiKeyVO apiKey = new ApiKeyVO();
		
		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/" + apiKey.getPublicBikeRealTimeRentApiKey()); /*Service Key*/
        urlBuilder.append("/" + "json"); /*type*/
        urlBuilder.append("/" + URLEncoder.encode("bikeList","UTF-8")); /*함수명*/
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("/" + URLEncoder.encode("1000", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
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
    }
}
