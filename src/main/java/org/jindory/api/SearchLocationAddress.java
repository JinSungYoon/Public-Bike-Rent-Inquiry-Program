package org.jindory.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jindory.domain.ApiKeyVO;
import org.jindory.domain.LocationAddressVO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jdk.internal.org.jline.utils.Log;

@Service
public class SearchLocationAddress {
	
	public static String searchPlace(String keyword) {
		
		ApiKeyVO apiKey = new ApiKeyVO();
		
		try {
			keyword = URLEncoder.encode(keyword,"UTF-8");
		}catch(UnsupportedEncodingException e) {
			throw new RuntimeException("encoding fail!",e);
		}
		
		String apiURL = "https://openapi.naver.com/v1/search/local.json?query="+keyword+"&display=1&start=1&sort=random";
		
		Map<String,String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id",apiKey.getNaverClientId());
		requestHeaders.put("X-Naver-Client-Secret", apiKey.getNaverClientSecret());
		String responseBody = get(apiURL,requestHeaders);
		
		System.out.println("네이버에서 받은 결과 = " + responseBody);
        System.out.println("-----------------------------------------");
        
        return responseBody;

	}
	
	
	private static String get(String apiUrl,Map<String,String> requestHeaders) {
		
		HttpURLConnection con = connect(apiUrl);
		
		try {
			con.setRequestMethod("GET");
			for(Map.Entry<String,String> header:requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}
			
			int responseCode = con.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK) {
				return readBody(con.getInputStream());
			}else{
				return readBody(con.getErrorStream());
			}
			
		}catch(IOException e ) {
			throw new RuntimeException("API 요청과 응답 실패");
		}finally {
			con.disconnect();
		}
	}
	
	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection)url.openConnection();
		}catch(MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : "+apiUrl,e);
		}catch(IOException e) {
			throw new RuntimeException("연결이 실패했습니다 . : "+apiUrl,e);
		}
	}
	
	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body,StandardCharsets.UTF_8);
		try(BufferedReader lineReader = new BufferedReader(streamReader)){
			StringBuilder responseBody = new StringBuilder();
			
			String line;
			while(( line = lineReader.readLine() ) != null) {
				responseBody.append(line);
			}
			
			return responseBody.toString();
			
		}catch(IOException e) {
	          throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}
	
	public static JSONArray getLocationAddressResultJson(String keyword) throws Exception{
		
		System.out.println("Input value : "+keyword);
		
		String result = searchPlace(keyword);
		
		System.out.println("Result : "+result);
		
		// Json형태의 String을  json으로 만들기
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject)parser.parse(result);
		
		// response
		JSONArray j_Row = (JSONArray) jsonObj.get("items");
		
		System.out.println("j_Row"+j_Row);
		
//		// 2. Gson --> 배열로 변경 List<LocationAddressVO>
//		Gson gson = new Gson();
//		List<LocationAddressVO> list = gson.fromJson(j_Row.toString(),new TypeToken<List<LocationAddressVO>>(){}.getType());
//		
//		System.out.println("Finall location return : "+list);
//		
		return j_Row;
		
	}
	
}
