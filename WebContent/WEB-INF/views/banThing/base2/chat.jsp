<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>  
<link rel="stylesheet" href="<c:url value="/resources/css/base2/chat.css"/>">
<div class="main1">
	<h2>방 제목:${vo.title}</h2>
	<div class="chatBox">
		
		<div id="chatLog" style="overflow:scroll;" class="chatLog">
		</div>
		<div id="chatSub">
			<input id="name" class="name" value="${sessionNick}" type="text" readonly>
			<input id="message" onkeydown="Enter_Check()" class="message" type="text" autocomplete="off" required="required">
			<button class="chat-button bg-light round-input" onclick="chat_submit()">전송</button>
			<button type="button" class="chat-button" onclick="chat_exit()">나가기</button>
		</div>
		<div id="chatInfo">
		</div>
		<!-- 거래 기능들 -->
		<div class="chatfunction">
			<div class="dealSetting">
			</div><br/>
			<c:if test="${sessionNick==vo.nick}">
				<button class="btn">방 설정</button>
			</c:if>
			<div id="dealState">
				하이룽~
			</div>
		</div>
		<div class="chatDealStart">
		미 진행 중...
		</div>
		<div id="userInfo">		    
	    </div>
	    <div id="roomBtn">
	    </div>
	    <!-- 방장일 경우 나오는 것 -->
	    <div id="modifyRoom">
	    <h1> 방 수정</h1>
			
			<!-- 웹 방 개설 셋팅 -->   <!-- onsubmit="return check()"  -->
				<form name="web_RoomSet" class="web_RoomSet" action="modifyChat" method="post" enctype="multipart/form-data">
	           		<input type="hidden" name="id" value="${sessionId}"/>
	           		<input type="hidden" name="num" value="${vo.num}"/>
	           		<input type="hidden" name="img" value="${vo.img}"/>
	           		<input type="text" class="btn" name="title" placeholder="방제목" value="${vo.title }"><br/>
					<select class="btn" name="options" id="optionsSelect">
						<option value="" >카테고리 설정</option>
						<option value="치킨" >치킨</option>
						<option value="햄버거" >햄버거</option>
						<option value="피자" >피자</option>
					</select><br/>
					<input type="text" class="btn" name="tag" placeholder="#해쉬태그, #이렇게, #작성하세요," value="${vo.tag }"><br/>
					<input type="text" class="btn" name="product" placeholder="상품이름" style="width:140px;" value="${vo.product}">
					<input type="text" class="btn" name="price" placeholder="상품가격" style="width:140px;" value="${vo.price }"><br/>
					<select class="btn" name="pay" id="paySelect" value="${vo.pay}" >
						<option value="">거래 방식</option>
						<option value="협의">협의</option>
						<option value="만나서">만나서</option>
						<option value="계좌이체">계좌이체</option>
						<option value="안전거래">안전거래</option>
					</select><br/>
					<select class="btn" name="gender" style="width:140px;" id="genderSelect" value="${vo.gender }" >
						<option value="">성별</option>
						<option value="무관">무관</option>
						<option value="여자">여자</option>
						<option value="남자">남자</option>
					</select>
					<select class="btn" name="personnel" style="width:140px;" id="personnelSelect" value="${vo.personnel }">
						<option value="">인원수</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
					</select>
					<input type="text" class="btn" name="place" id="place" placeholder="거래 장소" style="width:220px;" readonly="readonly" value="${vo.place}">
					<input type="hidden" name="placeInfo" id="placeInfo" >
					<input type="button" class="btn" value="검색" onclick="address_setting()" style="width:60px;"><br/>
					<input type="file" class="btn" name="orgImg" placeholder="상품 이미지" ><br/>
					<textarea name="content" >${vo.content}</textarea></br>
			        <button type="button" class="btn web_Exit" id="web_RoomExitBtn">닫기</button>
			        <button class="btn web_RoomOpen" id="web_RoomOpenBtn" onclick="submit" >수정</button>
	           </form>
	    </div>
	    <!-- 방장이 아닐 경우 -->
		    <div id="roomInfo">		
			    <h4>방정보</h4>
				방제 : ${vo.title}<br>
				카테고리 : ${vo.options}<br>  
				해쉬테그 : ${vo.tag}  <br>
				상품명 : ${vo.product}<br>
				가격 : ${vo.price}<br>  
				거래방식 : ${vo.pay}  <br>
				성별 : ${vo.gender}<br>  
				최대인원: ${vo.personnel}<br>  
				장소 : ${vo.place}  <br>
				이미지 : ${vo.img}<br>  
				<h4>글내용</h4> 
				${vo.content} 
				</br>
				<button type="button" class="btn web_Exit" id="web_RoomExitBtn">닫기</button>
		    </div>		    
		
	</div>
</div>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=a44d7ff9bef3120be5c58e8aa20ddfe0&libraries=clusterer,services"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	$(document).ready(function () {
		// option값으로 자동 셀렉 (selected)
		$("#paySelect").val("${vo.pay}");
		$("#optionsSelect").val("${vo.options}");
		$("#genderSelect").val("${vo.gender}");
		$("#personnelSelect").val("${vo.personnel}");
	});
	function dealStart(){
		var dealStart=open("dealStart.1?num=${vo.num}", "거래시작" , "toolbar=no,location=no,status=no, menubar=no, scrollbars=no, resizalbe=no, width=600px, height=750px,left=400px,top=200px");
		//거래시작시 중단버튼과 완료버튼 나옴
	}
	function chat_exit(){
		if("${vo.nick}"=="${sessionNick}"){
			var check=confirm("너가 나가면 방폭파되는게 괜찮 방구?");
			if(check){
				console.log("ㅎㅇㅋㅋ");
				window.location.href="chatBoom?num=${vo.num}";
			}else{
				return false;
			}
		}else{
			window.location.href="chatExit?nick=${sessionNick}&num=${vo.num}&id=${sessionId}";
		}
	}
	
	function chat_submit(){
		$.ajax({
			url:"chatSave",
	        type: 'get',
	        data: { num:"${vo.num}",nick:"${sessionNick}",mes:$("#message").val()},
	        dataType:'json', 
	        contentType:"application/json;charset=UTF-8",
			success:function(data){
				chat_Info();
	 		}
		});
		$("#message").val("");
		$("#chatLog").scrollTop($("#chatLog")[0].scrollHeight);
	}
	
	$(document).ready(function(){
		setInterval(chat_Info, 300);
		$("#chatLog").scrollTop($("#chatLog")[0].scrollHeight);
	})
	
	function Enter_Check(){
    if(event.keyCode == 13){
      chat_submit();
      $('#message').val("");
    }
	}
	function address_setting(){
	    new daum.Postcode({
	        oncomplete: function(data) {
	            $("#place").val(data["address"]);
	            console.log(data);
				var geocoder = new kakao.maps.services.Geocoder();
	        	geocoder.addressSearch(data.address,function(result, status){
	        		 if (status === kakao.maps.services.Status.OK) {
	        		        $("#placeInfo").val(result[0].x+"-"+result[0].y);
	        		        console.log(result[0].x+"-"+result[0].y);
	        		 }
	        	});
	        }
	    }).open();
	}
	var chatInfo="${vo.users}".split(",");
	var userProfiles="";
	var roomDetail="";
	// 방장일 경우
	if("${sessionNick}" == "${vo.nick}"){
		
		roomDetail += "<button onclick='roomModify()''=>방 정보 수정</button>";
		roomDetail += "<button onclick='roomInfo()''=>방 정보 보기</button>";
		for(i = 0; i < chatInfo.length-1; i++){
				var id="'"+chatInfo[i]+"'";
				userProfiles +=chatInfo[i]+'<button onclick="userProfile('+id+')">정보보기</button></br>';
		}
	// 방장이 아닐 경우
	}else{
		roomDetail += "<button onclick='roomInfo()''=>방 정보 보기</button>";
		for(i = 0; i < chatInfo.length-1; i++){
			var id="'"+chatInfo[i]+"'";
			userProfiles +=chatInfo[i]+'<button onclick="userProfile('+id+')">정보보기</button><br>';
		}
	}
	$("#userInfo").html(userProfiles);
	$("#roomBtn").html(roomDetail);
	
	// 유저정보보기
	function userProfile(nick){
		console.log(nick);
	    var url = "userProfile.1?nick="+nick;
	    var name = "userProfile";
	    var option = "width = 500, height = 500, top = 100, left = 200, location = no";
	    window.open(url, name, option);
	}
	function chat_Info(){
		$.ajax({
			url:"logInfo",
	        type: 'get',
	        data: { num:"${vo.num}",id:"${sessionId}",nick:"${sessionNick}"},
	        dataType:'json',
	        contentType:"application/json;charset=UTF-8",
			success:function(data){
				let chatLog="<p>공+지 : BTS 채팅방에 오신 걸 환영합니다 !</p>";
				var chatInfo="${vo.users}".split(",");
				var usersInfo="";
				data['log'].forEach(function(i){
					if(i.id=="${sessionNick}"){
						chatLog +="<p class='myMessage'>"+i.id+":"+i.mes+"</p>";
					}else if(i.id=="운영자"){
						chatLog +="<p style='color:red;'>"+i.id+":"+i.mes+"</p>";
					}else{
						chatLog +="<p>"+i.id+":"+i.mes+"</p>";
					}						
				})
				
				chatInfo.forEach(function(i){
					if(i=="${vo.nick}"){
						usersInfo +="<h4>"+i+"(방장)";
					}else{
						usersInfo +="<h4>"+i;
					}
				
					if(i=="${sessionNick}"){
						usersInfo+="(나)</h4> "
					}
					else{
						usersInfo+="</h4>";
					}
				})
				//거래 시작시 나오는 페이지

					dealState();
					$("#chatLog").html(chatLog);
					$("#chatInfo").html(usersInfo);
	 		}
		});
	}
	function dealState(){
		$.ajax({
			url:"dealinfo",
	        type: 'get',
	        data: { num:"${vo.num}"},
	        dataType:'json',
	        contentType:"application/json;charset=UTF-8",
			success:function(data){
				var vo=data['vo'];
				var users=vo['users'].split(',');
				var dealstate=vo['dealState'];
				var agree="";
				var count=0;
				var deal="";
				var sample="";
				users.forEach(function(i,index){
					//동의 묻는 중
					if(dealstate=="거래 전"){
						deal="<p style='color:red'>운영자:대화를 통해 거래를 물어보세요.</p>";
						deal+="<p style='color:red'>운영자:거래가 성사될시 하단에 거래 신청을 눌러주세요.</p>";
						if("${sessionNick}"=="${vo.nick}"){
							sample='<button class="btn" onclick="dealStart('+vo['num']+');">거래 신청</button><br/><br/>';
						}
					}
					if(dealstate=="동의 중"){
						deal="<p style='color:red'>운영자:거래 시작하기 앞서 동의여부를 묻도록 하겠습니다.</p>";
						deal+="<p style='color:red'>운영자:아래 상세페이지를 읽어주시고 동의/거절 버튼을 눌러주세요.</p>";
						deal+="<p style='color:red'>운영자:<a href='#' onclick='dealDetail()'>거래 상세페이지</a> </p>";
						if(i.includes(1)){
							count++;
							agree+="<p>"+i.split('1')[0]+"님 동의보감</p>";
						}
						if(count==users.length-1){
							setDealState("동의 완료",vo['num']);
						}
					}
					
					else if(dealstate=="동의 완료"){
						var state=["'"+"결제 중"+"'",vo['num']];
						agree="<p>거래 스타뚜!!!!!!!!~</p>";
						deal="<p style='color:red'>운영자:모든 사람이 동의를 허가하였습니다.</p>";
						deal+="<p style='color:red'>운영자:방장은 결제 시작 버튼을 눌러주세요.</p>";
						deal+="<p style='color:red'>운영자:<a href='#' onclick='dealDetail("+"1"+")'>거래 상세페이지</a> </p>";
						var param = [vo['num'],"'"+vo['product']+"'",vo['price'],users.length-1];
						if("${sessionNick}"=="${vo.nick}"){
							sample='<button class="btn" onclick="setDealState('+state+');">결제 시작</button><br/><br/>';
						}
					}
					else if(dealstate=="결제 중"){
						var state=[vo['num'],"'"+vo['product']+"'",vo['price'],users.length-1];
						deal="<p style='color:red'>운영자:오른쪽 하단에 결제 하기 버튼을 클릭하시고 결제를 해주세요.</p>";
						deal+="<p style='color:red'>운영자:비용은 n빵입니다 ㅋㅋ..ㅎㅎ 불만없제?ㅋㅋ</p>";
						deal+="<p style='color:red'>운영자:<a href='#' onclick='dealDetail("+"1"+")'>거래 상세페이지</a> </p>";
						sample='<button class="btn" id="dealPay" onclick="dealPay('+state+');">결제 하기</button><br/><br/>'; 
						if(i.includes(2)){
							count++;
							agree+="<p>"+i.split('1')[0]+"님 결제 쌉완료</p>";
						}
						if(count==users.length-1){
							setDealState("결제 완료",vo['num']);
						}
					}
					else if(dealstate=="결제 완료"){
						var state=["'"+"거래 완료"+"'",vo['num']];
						deal="<p style='color:red'>운영자:모든 결제가 완료되었습니다.</p>";
						deal+="<p style='color:red'>운영자:방장은 배송날짜를 명확히 알려주세요.</p>";
						deal+="<p style='color:red'>운영자:도착한 후 거래 완료 버튼을 눌러주세요.</p>";
						if("${sessionNick}"=="${vo.nick}"){
 							sample+='<button class="btn" onclick="setDealState('+state+');">거래 완료</button><br/>';
						}
					}
						//거래는 시작했는데 동의를 하지 않는경우.

					
			 		$(".dealSetting").html(sample);
					$(".chatDealStart").html(deal);
					$("#dealState").html(agree);
				})
	 		}
		});
	}
	//결제 상태 
	function setDealState(mes,a){
		$.ajax({
			url:"setDealState",
	        type: 'get',
	        data: { state:mes,num:a},
	        contentType:"application/json;charset=UTF-8",
			success:function(data){
	 		}
		});
	}
	
	//결제 창 켜기
	function dealPay(a,b,c,d){
		open("kakaoPay?num="+a+"&product="+b+"&price="+c+"&userCount="+d, "거래시작" , "toolbar=no,location=no,status=no, menubar=no, scrollbars=no, resizalbe=no, width=600px, height=750px,left=400px,top=200px");
	}
	//거래 상세정보
	function dealDetail(state){
		if(state==undefined){
			state=0;
		}
		open("dealDetail.1?num=${vo.num}&state="+state, "거래시작" , "toolbar=no,location=no,status=no, menubar=no, scrollbars=no, resizalbe=no, width=600px, height=750px,left=400px,top=200px");
	}
</script>

