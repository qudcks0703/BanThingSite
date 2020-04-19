package bts.basic.function;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// 아직 사용 미정 이 함수사용하면 /web-inf/views/mes.json 카카오톡으로 보낼 수 있음.
public class Alram {
	 public void sendMes(String token,HttpServletRequest request) {
		    String reqURL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
		    try {
		        URL url = new URL(reqURL);
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("POST");
		        conn.setDoOutput(true);
		        System.out.println(token);
		        //conn.setRequestProperty("Content-Type", "application/json");
		        conn.setRequestProperty("Authorization", "Bearer " + token);
		        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		        
		    	JsonParser parser = new JsonParser();
		    	String path=request.getRealPath("/WEB-INF/views/banThing/mes.json");
		        Object obj = parser.parse(new FileReader(path));
		        
		        JsonObject jo = (JsonObject)obj;
		        String json=jo.toString();
		        
		        OutputStreamWriter bw =new OutputStreamWriter(conn.getOutputStream());
	            StringBuilder sb = new StringBuilder();
	            sb.append("template_object=");
	            sb.append(json);
	            System.out.println(sb);
	            bw.write(sb.toString());
	            bw.flush();
		        
		        int responseCode = conn.getResponseCode();
		        System.out.println("responseCode : " + responseCode);
		        		
	            bw.close();
	            conn.disconnect();	


		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	 }
	 
	 public Map kakaoPay(String title,String price,int userCount) {
		 String reqURL = "https://kapi.kakao.com/v1/payment/ready";
		 Map map=null;
		 try {
			 URL url = new URL(reqURL);
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 conn.setRequestMethod("POST");
			 conn.setDoOutput(true);
			 conn.setRequestProperty("Content-Type", "application/json");
			 conn.setRequestProperty("Authorization", "KakaoAK 291c8e8862afaf2418a8b9588b8529d7");
			 conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			 
			 OutputStreamWriter bw =new OutputStreamWriter(conn.getOutputStream());
			 StringBuilder sb = new StringBuilder();
			 sb.append("cid=TC0ONETIME");
			 sb.append("&partner_order_id=partner_order_id");
			 sb.append("&partner_user_id=partner_user_id");
			 sb.append("&item_name="+title);
			 sb.append("&quantity=1");
			 sb.append("&total_amount="+Integer.parseInt(price)/userCount);
			 sb.append("&vat_amount=200");
			 sb.append("&tax_free_amount=0");
			 sb.append("&approval_url=http://192.168.0.136:8080/BTS/banThing/PaySuccess");
			 sb.append("&fail_url=http://192.168.0.136:8080/BTS/banThing/PaySuccess");
			 sb.append("&cancel_url=http://192.168.0.136:8080/BTS/banThing/PaySuccess");
			 bw.write(sb.toString());
			 bw.flush();
			 
			 int responseCode = conn.getResponseCode();
			 System.out.println("접속 상태 코드 : " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            
            while ((line = br.readLine()) != null) {
                result += line;
            }
            JSONParser parser=new JSONParser();
            JSONObject json=(JSONObject)parser.parse(result);
            String redirect_url=(String)json.get("next_redirect_pc_url");
            String tid=(String)json.get("tid");
            map=new HashMap();
            map.put("redirect_url", redirect_url);
            map.put("tid", tid);
            
			 bw.close();
			 conn.disconnect();	
			 
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 
		 return map;
	 }
	 
	 public void paySuccess(String pg_token,String tid) {
		 String reqURL = "https://kapi.kakao.com/v1/payment/approve";
		 try {
			 URL url = new URL(reqURL);
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 conn.setRequestMethod("POST");
			 conn.setDoOutput(true);
			 conn.setRequestProperty("Content-Type", "application/json");
			 conn.setRequestProperty("Authorization", "KakaoAK 291c8e8862afaf2418a8b9588b8529d7");
			 conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			 
			 OutputStreamWriter bw =new OutputStreamWriter(conn.getOutputStream());
			 StringBuilder sb = new StringBuilder();
			 sb.append("cid=TC0ONETIME");
			 sb.append("&tid="+tid);
			 sb.append("&partner_order_id=partner_order_id");
			 sb.append("&partner_user_id=partner_user_id");
			 sb.append("&pg_token="+pg_token);
			 bw.write(sb.toString());
			 bw.flush();
			 
			 int responseCode = conn.getResponseCode();
			 System.out.println("접속 상태 코드 : " + responseCode);
			 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			 String line = "";
			 String result = "";
			 
			 while ((line = br.readLine()) != null) {
				 result += line;
			 }
//			 JSONParser parser=new JSONParser();
//			 JSONObject json=(JSONObject)parser.parse(result);
			 
			 bw.close();
			 conn.disconnect();	
			 
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }
	
		    
	
}
