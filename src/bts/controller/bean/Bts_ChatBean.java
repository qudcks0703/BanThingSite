package bts.controller.bean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jdt.internal.compiler.ast.JavadocSingleNameReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import bts.basic.function.ChatMethod;
import bts.model.dao.Bts_ChatDAO;
import bts.model.dao.Bts_DealDAO;
import bts.model.vo.Bts_ChatVO;
import bts.model.vo.Bts_DealVO;

//梨꾪똿湲곕뒫 愿��젴 而⑦듃濡ㅻ윭
@Controller
@RequestMapping("/banThing/")
public class Bts_ChatBean {
	
	@Autowired
	private Bts_ChatDAO chatDAO;
	@Autowired
	private Bts_DealDAO dealDAO;
	
	public HttpServletRequest request=null;
	public Model model = null;
	public HttpSession session=null;
	String uri=null;
	String nick=null;
	String id=null;
	String path=null;
	ChatMethod cm=new ChatMethod();
	
	@ModelAttribute
	public void reqres(HttpServletRequest request, Model model,HttpSession session) {
		this.request=request;
		this.model = model;
		this.session=session;
		uri=request.getRequestURI();
		nick=(String)session.getAttribute("sessionNick");
		id=(String)session.getAttribute("sessionId");
		path=request.getRealPath("/WEB-INF/chatRoom/");
	}
	
	//梨꾪똿諛�  �젒�냽�븷 怨�
	@RequestMapping("chat.2")
	public String chat(int num) throws Exception{
		//users而щ읆�뿉 �꽭�뀡�뿉 ���옣�맂 �땳怨� �뀒�씠釉붾쾲�샇 蹂대궡�꽌 ex)理쒕퀝李�,瑗대줈 ���옣
		int check=chatDAO.setUsers(nick+",",num);
		//�듅�젙 �뒠�뵆 媛��졇�삤湲�.
		Bts_ChatVO vo=chatDAO.getUniqueChatInfo(num);
		//�럹�씠吏��뿉 chatVo蹂대궡湲�
		model.addAttribute("vo", vo);
		//check==1 媛믩�寃� �꽦怨�
		if(check == 1) {
			//ChatMethod�뿉�엳�뒗 saveJson �떎�뻾�븯�뿬 蹂대궡二쇨린.
			cm.saveJson(path,num, id, nick, nick+"�떂猿섏꽌 �엯�옣�븯�뀲�뒿�땲�떎.");
			return uri.split("/")[3];
		}else {
			return uri.split("/")[3];
		}
	}
	
	//諛� 媛쒖꽕
	@RequestMapping("submit")
	public void createChat (Bts_ChatVO vo, MultipartHttpServletRequest multireq,HttpServletResponse response) throws Exception {
		int num = 0;
		if (request.getMethod().equals("POST")) {
			MultipartFile mf = null;
			String newName = null;
			try {
				// # �뙆�씪�씠由� 以묐났泥섎━  :  �궇吏� + �삤由ъ��꼸�뙆�씪紐�
				mf = multireq.getFile("orgImg");									//0. �뙆�씪 �젙蹂� �떞湲�
				String orgName = mf.getOriginalFilename();//1. �삤由ъ��꼸 �뙆�씪紐�
				String imgName = orgName.substring(0, orgName.lastIndexOf('.')); 	//2. �뙆�씪紐낅쭔 異붿텧
				String ext = orgName.substring(orgName.lastIndexOf('.'));			//3. �솗�옣�옄 異붿텧
				long date = System.currentTimeMillis();								//4. �궇吏� 諛쏆븘�삤湲�
				newName = date + imgName + ext;										//5. 理쒖쥌 �뙆�씪�씠由�
				String nick=(String)session.getAttribute("sessionNick");
				// # DB�뿉 form ���옣
				vo.setUsers(nick+",");
				vo.setImg(newName);
				vo.setNick(nick);
				vo.setTag(vo.getTag().replaceAll(" ", ""));
				//諛⑷컻�꽕
				chatDAO.createChat(vo);
				//諛� 踰덊샇�씘�뱷
				num=chatDAO.getMaxNum();
				
				Bts_DealVO dealvo=new Bts_DealVO();
				dealvo.setDealState("嫄곕옒 �쟾");
				dealvo.setId(vo.getId());
				dealvo.setNum(num);
				dealvo.setPlace(vo.getPlace());
				dealvo.setPrice(vo.getPrice());
				dealvo.setProduct(vo.getProduct());
				dealvo.setUsers(vo.getNick()+",");
				dealvo.setUrl("");
				dealvo.setTid("");
				//嫄곕옒 媛쒖꽕
				dealDAO.createDeal(dealvo);
				
		        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path+num+".json"));
				// # �꽌踰꾩뿉 �씠誘몄� ���옣 : WEB-INF  >  chatImg
				String path1 = multireq.getRealPath("/resources/chatImg");
				String imgPath = path1+ "\\" + newName;
				File copyFile = new File(imgPath);
				mf.transferTo(copyFile);
				
				model.addAttribute("createChat","success");
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				response.sendRedirect("chat.2?num="+num);
			}
		}
	}

	// 諛� �닔�젙
	   @RequestMapping("modifyChat")
	   public void modifyChat(Bts_ChatVO vo, MultipartHttpServletRequest multireq,HttpServletResponse response) {
		    MultipartFile mf = null;
			String newName = null;
			
			// 1. 湲곗〈�씠誘몄� �궘�젣
			mf = multireq.getFile(vo.getImg());
			String path = multireq.getRealPath("chatImg");
			String imgPath = path+ "\\" + vo.getImg();
			File copyFile = new File(imgPath);
			copyFile.delete();
			
			// 2. �씠誘몄� �떎�떆 �떞湲�.
			mf = multireq.getFile("orgImg");		
			String orgName = mf.getOriginalFilename();// �삤由ъ��꼸 �뙆�씪紐�
			String imgName = orgName.substring(0, orgName.lastIndexOf('.')); 	// �뙆�씪紐낅쭔 異붿텧
			String ext = orgName.substring(orgName.lastIndexOf('.'));			// �솗�옣�옄 異붿텧
			long date = System.currentTimeMillis();								// �궇吏� 諛쏆븘�삤湲�
			newName = date + imgName + ext;
			vo.setImg(newName);
				// # �꽌踰꾩뿉 �씠誘몄� ���옣 : WEB-INF  >  chatImg
				path = multireq.getRealPath("chatImg");
				imgPath = path+ "\\" + newName;
				copyFile = new File(imgPath);
				try {
					mf.transferTo(copyFile);
					// 諛� �젙蹂� �닔�젙
					chatDAO.modifyChat(vo);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}		
			try {
				response.sendRedirect("chat.2?num="+vo.getNum());
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		   
	   }

	//諛� �룺�뙆~ 遺�!!!
	@RequestMapping("chatBoom")
	public void chatExit(int num,HttpServletResponse response) throws IOException{
		System.out.println("遺먮텗遺�~");
		chatDAO.chatBoom(num);
		response.sendRedirect("index.2");
	}
	

	//梨꾪똿 ���옣
	@RequestMapping("chatSave")
	public void chatSave(int num,String mes,String nick) throws Exception{
		//諛붽씀吏�留덉깦 梨꾪똿�� �땳�쑝濡� 蹂대궪嫄곗엫 �뀑�뀑
		cm.saveJson(path,num, nick, nick,mes);
	}
	
	//諛� �굹媛��뒗 硫붿꽌�뱶
	@RequestMapping("chatExit")
	public void chatExit(String nick,String id,int num,HttpServletResponse response) throws Exception{
	    
		int check=chatDAO.chatExit(nick,num);
		//check==1 �굹媛�吏�
		if(check==1) {
			  cm.saveJson(path,num, id, nick,nick+"�떂猿섏꽌 �눜�옣�븯�뀲�뒿�땲�떎.");
		}
		response.sendRedirect("index.2");
	}
   @ResponseBody
   @RequestMapping("logInfo")
   public void logInfo(int num,String id,String nick,String entrance,HttpServletResponse response) throws Exception{
	  response.setHeader("Content-Type", "application/xml"); 
      response.setContentType("text/html;charset=UTF-8"); 
      response.setCharacterEncoding("utf-8");
      JSONObject jo=null;
      JSONParser parser = new JSONParser();
      JSONArray log=new JSONArray();
  	  JSONObject chat=new JSONObject();
      String path=request.getRealPath("/WEB-INF/chatRoom/");
      try {
    	  jo = (JSONObject)parser.parse(new FileReader(path+num+".json"));
      }catch(Exception e) {
      	  log=new JSONArray();
      	  chat=new JSONObject();
      	  jo=new JSONObject();
      	  jo.put("num", num);
      	  chat.put("id", nick);
      	  chat.put("mes", nick);
      	  log.add(chat);
          jo.put("log", log);
          FileWriter save = new FileWriter(path+num+".json"); 
          save.write(jo.toJSONString()); 
          save.flush();
          save.close(); 
      }
        
      response.getWriter().print(jo.toString());
   }
   
   //�궡媛�留뚮뱺 梨꾪똿諛�,�궡媛� �뱾�뼱媛� 梨꾪똿諛� 李얘린
	@RequestMapping(value = "mychat", headers="Accept=*/*",  produces="application/json")
	@ResponseBody
	public Map mychat(String num) {
		Map map=new HashMap(); 
		List chatList=new ArrayList();
	   if(num != null) {
		   if(num.equals("1")) {
			   chatList = chatDAO.mychat(nick);
		   }else if(num.equals("2")){
			   chatList = chatDAO.inchat(nick,id);
		   }
	   }else {
		   num = "0";
	   }
	   map.put("myChat", chatList);
	   return map;
   }
	
	@RequestMapping(value = "chatinfo", headers="Accept=*/*",  produces="application/json")
	@ResponseBody
	public Map ChatInfo(String[] search){
		List chatList=new ArrayList();
		chatList=chatDAO.getChatInfo(search[3],search[4]);
		
		Map map=new HashMap(); 
		map.put("chatList", chatList);
		map.put("lat", search[0]);
		map.put("lng", search[1]);
		map.put("level", search[2]);
		map.put("options", search[3]);
		map.put("keyword", search[4]);
		 
		return map;
	}
	@RequestMapping(value = "dealinfo", headers="Accept=*/*",  produces="application/json")
	@ResponseBody
	public Map DealInfo(int num){
		
		Bts_DealVO vo= dealDAO.getUniqueDeal(num);
		Map map=new HashMap(); 
		map.put("vo", vo);
		
		return map;
	}

}
