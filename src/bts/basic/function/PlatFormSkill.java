package bts.basic.function;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import bts.model.dao.Bts_ChatDAO;
import bts.model.vo.Bts_ChatVO;

@Controller
@RequestMapping("/banThing/")
public class PlatFormSkill {
	Logins login=new Logins();
	Alram alram=new Alram();
	
	HttpServletRequest request=null;
	HttpServletResponse response=null;
	HttpSession session=null;
	Model model=null;
	
	// 아래 어노테이션을 이용하여 각 변수 재활용 
	//모델어트리뷰트를 사용하게 되면 맵핑에 해당하는 메소드가 실행되기 전에 가장 먼저 실행 됨
	@ModelAttribute
	public void setting(HttpServletResponse response,HttpSession session,HttpServletRequest request,Model model) {
		this.request=request;
		this.response=response;
		this.session=session;
		this.model=model;
	}
	
	//카카오 로그인
	@RequestMapping("kakaologin")
	public String kakaologin(String code) throws URISyntaxException, MalformedURLException{
		
		String token= login.kakaoToken(code);
		session.setAttribute("kakaotoken", token);
		Map userInfo=login.kakaoInfo(token);
		request.setAttribute("userInfo", userInfo);
		return "/banThing/logins";
	}
	
	//카카오 알림 
	@RequestMapping("kakaoAlram")
	public String kakaoAlram() throws URISyntaxException, MalformedURLException{
		
		String token=(String)session.getAttribute("kakaotoken");
		alram.sendMes(token, request);
		return "index.2";
	}


}
