package org.jindory.batch;

import org.jindory.api.PublicBikeRent;
import org.jindory.domain.ApiKeyVO;
import org.jindory.domain.Criteria;
import org.jindory.domain.PublicBikeFavoritesVO;
import org.jindory.service.PublicBikeMemberService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;

import lombok.extern.log4j.Log4j;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Component
@Log4j
public class EmailProgram {
		
	@Autowired 
	private PublicBikeMemberService publicBikeMemberService;
	
	private static final int THREAD_POOL_SIZE = 10;
	
	private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	
	public void gmailSend() throws Exception {
		
		ApiKeyVO privateInfo = new ApiKeyVO();
		
		// 발신자 이메일정보를 입력.
		String email = privateInfo.getGMail();
		String password = privateInfo.getGPassword();
		// SMTP 서버 정보를 설정.
		Properties props = new Properties();
		// 메일 프로토콜을 Simple Mail Transfer Protocol을 사용할것이기 때문에 smtp로 설정
		props.setProperty("mail.transport.protocol", "smtp");
		// 메일을 gmail로 발송할것이기 때문에 smtp.gmail.com으로 설정.
		props.setProperty("mail.host","smtp.gmail.com");
		// ID, Password에 대한 설정 필요
		props.setProperty("mail.smtp.auth","true");
		// port는 465
		props.put("maile.smtp.port","465");
		//SSL 사용일때
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", "587"); 
		props.put("mail.smtp.ssl.enable", "true");
		// ID와 Password를 설정하고 seesion을 생성
		Session session = Session.getInstance(props,new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email,password);
			}
		});

		try {
			
			List<PublicBikeFavoritesVO> alertFavoritesList = new ArrayList<PublicBikeFavoritesVO>();
			alertFavoritesList = publicBikeMemberService.getAlertFavorites();
			
			log.info(alertFavoritesList);
			
			JSONArray newList = new JSONArray();
			JSONArray sortedList = new JSONArray();
			List<String> publicParkingList = new ArrayList<String>();
			String bodyContent = "<table border='1'><th>정류장 이름</th><th>자전거 거치대수</th><th>거리(m)</th>";
			
			long start = System.currentTimeMillis(); //시작하는 시점 계산
			
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
			
			long end = System.currentTimeMillis(); //프로그램이 끝나는 시점 계산
			System.out.println( "실행 시간 : " + ( end - start )/1000.0 +"초"); //실행 시간 계산 및 출력
			
			DecimalFormat df = new DecimalFormat("###.##");
			
			JSONArray alertJson = new JSONArray();
			
			for(int idx=0;idx<result.size();idx++) {
				JSONObject obj = (JSONObject) result.get(idx);
				String stationId = (String) obj.get("stationId");
				String noticeBikeCount = (String) obj.get("rackTotCnt");
				int noticeBikeNum = Integer.parseInt(noticeBikeCount);
				for(int innerLoop = 0;innerLoop<alertFavoritesList.size();innerLoop++) {
					// 알림줘야하는 정류장 이름과 같고 알림 줘야하는 자전거 대수보다 적게 거치되어 있다면,  
					if(alertFavoritesList.get(innerLoop).getStationId().equals(stationId) && noticeBikeNum < alertFavoritesList.get(innerLoop).getNoticeBikeNum() ) {
						log.info("Station Id : "+stationId+"Rack Bike Num : "+noticeBikeNum);
						//alertFavoritesList.remove(innerLoop);
						alertJson.add(result.get(idx));
					}
				}
			}
			
			for(int loop=0;loop<alertJson.size();loop++) {
				// 거리 기준으로 따릉이 정류소 정렬.
				sortedList = sortDistanceJson(result,(JSONObject) alertJson.get(loop),"stationLatitude","stationLongitude","asc");
							
				for(int idx=0;idx<10;idx++) {
					JSONObject sortedObj = (JSONObject)sortedList.get(idx);
					
					String stationName = (String)sortedObj.get("stationName");
					String parkingBikeTotCnt = (String)sortedObj.get("parkingBikeTotCnt");
					String latitude = (String)sortedObj.get("stationLatitude");
					String longitude = (String)sortedObj.get("stationLongitude");
					int parkingBikeCount = Integer.parseInt(parkingBikeTotCnt);
					double lat = Double.parseDouble(latitude);
					double lng = Double.parseDouble(longitude);
					
					JSONObject obj =(JSONObject) alertJson.get(0); 
					String targetLat = (String) obj.get("stationLatitude");
					String targetLng = (String) obj.get("stationLongitude");
					double targetlat = Double.parseDouble(targetLat);
					double targetlng = Double.parseDouble(targetLng);
					double distance =  distance(targetlat,targetlng,lat,lng,"km");
					
					DecimalFormat distanceFormat = new DecimalFormat("#,###.##");

					String dist = distanceFormat.format(distance*1000);
					
					if(distance<alertFavoritesList.get(loop).getNoticeScope() && parkingBikeCount>alertFavoritesList.get(loop).getNoticeBikeNum()) {
						bodyContent = bodyContent + "<tr><td>"+stationName+"</td><td>"+parkingBikeTotCnt+"</td><td>"+dist+"</td>";
					}
					
				}
				
				bodyContent = bodyContent + "</table>";
			    
				System.out.println(bodyContent);
				
				// 메일 메시지를 만들기 위한 클래스 생성
				MimeMessage message = new MimeMessage(session);
				// 송신자 설정
				message.setFrom(getAddress(email));
				// 수신자 설정
				message.addRecipients(Message.RecipientType.TO, "yjs445566@naver.com");
				
				// 메일 제목을 설정
				message.setSubject("따릉이 보유현황 Email");
				// 메일 내용을 설정을 위한 클래스 설정.
				message.setContent(new MimeMultipart());
				
				// 메일 내뇽을 위한 multipart 클래스를 받아온다.(위 new MimeMultipart()로 넣은 클래스)
				Multipart mp = (Multipart) message.getContent();
				// html 형식으로 본문을 작성해서 바운더리에 넣습니다.
				mp.addBodyPart(getContents("<html><head></head><body>"+"<span>요청하신 지역의 따릉이 보유 현황입니다</span>"+bodyContent+"</body></html>"));

				Transport.send(message);
				
			}
			
			
			/*
			for(int loop=0;loop<list.size();loop++) {
				JSONObject obj = (JSONObject) list.get(loop);			
				
				String stationLatitude = (String)obj.get("stationLatitude");
				String stationLongitude = (String)obj.get("stationLongitude");
				double stLat = Double.parseDouble(stationLatitude);
				double stLog = Double.parseDouble(stationLongitude);
				
				DecimalFormat df = new DecimalFormat("###.##");
				
				obj.put("distance",df.format(distance(37.57731628,126.90296936,stLat,stLog,"meter")));
				newList.add(obj);
			}
			
			sortedList = sortJson(newList,"distance","asc");
			
			System.out.println("============================== 정렬후  ==============================");
			for(int loop=0;loop<sortedList.size();loop++) {
			JSONObject sortedObj = (JSONObject)sortedList.get(loop);
			
			String stationName = (String)sortedObj.get("stationName");
			String parkingBikeTotCnt = (String)sortedObj.get("parkingBikeTotCnt");
			String latitude = (String)sortedObj.get("stationLatitude");
			String longitude = (String)sortedObj.get("stationLongitude");
			String distance = (String)sortedObj.get("distance");
			double interval = Double.parseDouble(distance);
			
			if(interval<500.0) {
					System.out.println(sortedObj);
					publicParkingList.add("정류장 이름 : "+stationName+"\t/\t"+"자전거 거치대수 : "+parkingBikeTotCnt+"\t/\t"+"거리 : "+distance+"m"+"\n");
					bodyContent = bodyContent + "<tr><td>"+stationName+"</td><td>"+parkingBikeTotCnt+"</td><td>"+distance+"</td>";
					System.out.println(publicParkingList.get(publicParkingList.size()-1));
					System.out.println();
				}
			}
			
			bodyContent = bodyContent + "</table>";
		    
			System.out.println(bodyContent);
			
			// 메일 메시지를 만들기 위한 클래스 생성
			MimeMessage message = new MimeMessage(session);
			// 송신자 설정
			message.setFrom(getAddress(email));
			// 수신자 설정
			message.addRecipients(Message.RecipientType.TO, "yjs445566@naver.com");
			
			// 메일 제목을 설정
			message.setSubject("따릉이 보유현황 Email");
			// 메일 내용을 설정을 위한 클래스 설정.
			message.setContent(new MimeMultipart());
			
			// 메일 내뇽을 위한 multipart 클래스를 받아온다.(위 new MimeMultipart()로 넣은 클래스)
			Multipart mp = (Multipart) message.getContent();
			// html 형식으로 본문을 작성해서 바운더리에 넣습니다.
			mp.addBodyPart(getContents("<html><head></head><body>"+"<span>요청하신 지역의 따릉이 보유 현황입니다</span>"+bodyContent+"</body></html>"));
			*/
			//Transport.send(message);
			
		}catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	// String으로 된 메일 주소를 Address 클래스로 변환
	public Address getAddress(String email) throws AddressException {
		return new InternetAddress(email);
	}
	
	// 메일의 본문 내용 설정
	public BodyPart getContents(String html)throws MessagingException{
		BodyPart mbp = new MimeBodyPart();
		mbp.setContent(html,"text/html; charset=utf-8");
		return mbp;
	}
	
	public static JSONArray sortDistanceJson(JSONArray list,JSONObject target,String item1,String item2,String order)
    {
		JSONArray sortedJsonArray = new JSONArray();
		JSONArray jsonValues = new JSONArray();
		int direction;
		
		if(order.equals("asc")) {
			direction = -1;
		}else {
			direction = 1;
		}
		
        for (int index = 0; index < list.size(); index++) {
            jsonValues.add(list.get(index));
        }
        
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            
        	// 정렬할 type을 KEY_NAME1,KEY_NAME2에 정의
            String KEY_NAME1 = item1;
            String KEY_NAME2 = item2;
            
            @Override
            public int compare(JSONObject a, JSONObject b) {
            	// 비교할 값들 선언 valA,valB 비교할 값들이 숫자일 경우를 대비하여 A,B 선언 
            	String LatA = new String();
                String LngA = new String();
                String LatB = new String();
                String LngB = new String();
                float latA = 0.0f;
                float lngA = 0.0f;
                float latB = 0.0f;
                float lngB = 0.0f;
                
                try {
                	// JSONObject의 KEY_NAME에 해당하는 값 저장
                	LatA = (String) a.get(KEY_NAME1);
                	LngA = (String) a.get(KEY_NAME2);
                    LatB = (String) b.get(KEY_NAME1);
                    LngB = (String) b.get(KEY_NAME2);
                    // 만일 가져온 값이 숫자일 경우 A,B에 저장
                    if(LatA.matches("[+-]?\\d*(\\.\\d+)?") && LngA.matches("[+-]?\\d*(\\.\\d+)?") && LatB.matches("[+-]?\\d*(\\.\\d+)?") && LngB.matches("[+-]?\\d*(\\.\\d+)?")) {
                    	latA = Float.parseFloat(LatA);
                        lngA = Float.parseFloat(LngA);
                        latB = Float.parseFloat(LatB);
                        lngB = Float.parseFloat(LngB);
                    }
                    
                } 
                catch (JsonParseException e) {
                    //do something
                }
                	// 입력된
	                String stationLatitude = (String)target.get("stationLatitude");
					String stationLongitude = (String)target.get("stationLongitude");
					double targetLat = Double.parseDouble(stationLatitude);
					double targetLng = Double.parseDouble(stationLongitude);
					double distanceA = Math.pow(targetLat-latA,2) + Math.pow(targetLng-lngA,2);
					double distanceB = Math.pow(targetLat-latB,2) + Math.pow(targetLng-lngB,2);
					/*
					if(distanceA>distanceB) {
						return -1*direction;
					}else if(distanceA<distanceB) {
						return 1*direction;
					}else {
						return 0*direction;
					}
					*/
					if(distance(targetLat,targetLng,latA,lngA,"m")>distance(targetLat,targetLng,latB,lngB,"m")) {
						return -1*direction;
					}else if(distance(targetLat,targetLng,latA,lngA,"m")<distance(targetLat,targetLng,latB,lngB,"m")) {
						return 1*direction;
					}else {
						return 0*direction;
					}
                }
        });
        
        for (int i = 0; i < jsonValues.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }
        
        return sortedJsonArray;
    }
	
	public static JSONArray sortJson(JSONArray list,String item,String order)
    {
		JSONArray sortedJsonArray = new JSONArray();
		JSONArray jsonValues = new JSONArray();
		int direction;
		
		if(order.equals("asc")) {
			direction = -1;
		}else {
			direction = 1;
		}
		
        for (int index = 0; index < list.size(); index++) {
            jsonValues.add(list.get(index));
        }
        
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            
        	// 정렬할 type을 KEY_NAME에 정의
            String KEY_NAME = item;
            
            @Override
            public int compare(JSONObject a, JSONObject b) {
            	// 비교할 값들 선언 valA,valB 비교할 값들이 숫자일 경우를 대비하여 A,B 선언 
            	String valA = new String();
                String valB = new String();
                float A = 0.0f;
                float B = 0.0f;
                
                try {
                	// JSONObject의 KEY_NAME에 해당하는 값 저장
                	
                	valA = (String) a.get(KEY_NAME);
                    valB = (String) b.get(KEY_NAME);
                    // 만일 가져온 값이 숫자일 경우 A,B에 저장
                    if(valA.matches("[+-]?\\d*(\\.\\d+)?") && valA.matches("[+-]?\\d*(\\.\\d+)?")) {
                    	A = Float.parseFloat(valA);
                        B = Float.parseFloat(valB);
                    }
                    
                } 
                catch (JsonParseException e) {
                    //do something
                }
                // 입력한 값이 숫자일 경우 숫자 비교
                if(valA.matches("[+-]?\\d*(\\.\\d+)?") && valB.matches("[+-]?\\d*(\\.\\d+)?")) {
                	if(A>B) {
                    	return -1*direction;
                    }else if(A<B) {
                    	return 1*direction;
                    }else {
                    	return 0*direction;
                    }
                }else {		// 입력한 값이 문자일 경우 문자비교  valA가 valB보다 값이 크면 양수 valA와 valB가 같으면 0 valA가 valB보다 값이 작으면 음수 반환
                	if(order.equals("asc")) {
                		return valA.compareTo(valB);
                	}else {
                		return valB.compareTo(valA);
                	}
                }
                
                }
        });
        
        for (int i = 0; i < jsonValues.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }
        
        return sortedJsonArray;
    }
	
	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
         
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
         
        if (unit == "km") {
            dist = dist * 1.609344;
        } else if(unit == "m"){
            dist = dist * 1609.344;
        }
 
        return (dist);
    }
     
 
    // This function converts decimal degrees to radians
    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
     
    // This function converts radians to decimal degrees
    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
    
    @Scheduled(cron="0 0/10 * * * *")
    public void emailBatch()throws Exception{
    	gmailSend();
    }
    
	
}
