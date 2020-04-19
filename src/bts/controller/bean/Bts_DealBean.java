package bts.controller.bean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

import bts.basic.function.Alram;
import bts.model.dao.Bts_ChatDAO;
import bts.model.dao.Bts_DealDAO;
import bts.model.vo.Bts_ChatVO;
import bts.model.vo.Bts_DealVO;

//전체적인 사이트 컨트롤러
@Controller
@RequestMapping("/banThing/")
public class Bts_DealBean {
	
	@Autowired
	Bts_ChatDAO chatDAO=null;
	@Autowired
	Bts_DealDAO dealDAO=null;
	
	Alram alram=new Alram();
	HttpServletRequest request=null;
	HttpServletResponse response=null;
	HttpSession session=null;
	Model model=null;
	String uri=null;
	String tid=null;  
	int num=0;
	String nick=null;
	
	@ModelAttribute
	public void setting(HttpServletResponse response,HttpSession session,HttpServletRequest request,Model model) {
		this.request=request;
		this.response=response;
		this.session=session;
		this.model=model;
		uri=request.getRequestURI();
	}
	
	//거래 시작하는 메서드
	@RequestMapping("dealStart.1")
	public String dealStart(int num,Bts_DealVO Dealvo) {
		//겟 접근시 해당 num을 사용하여 vo를 가져감
		if(request.getMethod().equals("GET")) {
			Bts_ChatVO vo= chatDAO.getUniqueChatInfo(num);
			model.addAttribute("vo", vo);
		}
		//포스트 접근시 거래전 ->거래후 최병찬,->최병찬0, 으로 변경
		else {
			System.out.println("값 성공");
			//거래 중으로 상태변경
			
			//chat vo가져오기
			Bts_ChatVO vo=chatDAO.getUniqueChatInfo(Dealvo.getNum());
			
			Dealvo.setDealState("동의 중");
			String[] users=vo.getUsers().split(",");
			String update_Users="";
			//0붙여서 동의 상황 보여주기  0->비동의 1->동의
			for(String i : users) {
				i=i+"0,";
				update_Users +=i;
			}
			
			Dealvo.setUsers(update_Users);
			dealDAO.dealStart(Dealvo);
		}
		return uri.split("/")[3];
	}
	
	//거래 상세사항 보여주는 메서드
	@RequestMapping("dealDetail.1")
	public String dealDetail(int num) {
		
		//겟방식 접근시 vo보내주기
		if(request.getMethod().equals("GET")) {
			Bts_DealVO vo= dealDAO.getUniqueDeal(num);
			model.addAttribute("vo", vo);
		}else {
			String nick=(String)session.getAttribute("sessionNick");
			//0붙여서 현재 돈안 넣은 상태인 것 알려주기.
			dealDAO.dealStartUpdate(num,nick);
		}
		
		return uri.split("/")[3];
	}
	@RequestMapping("kakaoPay")
	public String kakaoPay(String product,String price,int num,int userCount) {
		Map map=alram.kakaoPay(product,price,userCount);
		tid=(String)map.get("tid");
		this.num=num;
		this.nick=(String)session.getAttribute("sessionNick");
		request.setAttribute("url", map.get("redirect_url"));
		return "banThing/kakaoPay";
	}
	
	@RequestMapping("PaySuccess")
	public void PaySuccess(String pg_token) throws IOException {
		alram.paySuccess(pg_token, tid);
		
		dealDAO.dealMoneyUpdate(num, nick);
		PrintWriter io=response.getWriter(); io.print("<script>");	
		io.print("self.close();"); 
		io.print("</script>");
		 
	}
	@RequestMapping("setDealState")
	public String setDealState(String state,int num) {
		System.out.println("현재 거래상태는?"+state);
		dealDAO.setDealState(state,num);
		return uri.split("/")[3];
	}
	
	

}
