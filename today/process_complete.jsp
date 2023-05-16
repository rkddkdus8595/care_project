<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB2"%>
<%@ page import="org.json.simple.JSONObject" %>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String phone = request.getParameter("phone"); // 핸드폰 번호
	String date = request.getParameter("date"); // 접수 날짜
	String oneContent = request.getParameter("oneContent"); // 내용 
	String twoContent = request.getParameter("twoContent"); // 내용 
	String threeContent = request.getParameter("threeContent"); // 내용 
	String kind = request.getParameter("kind");
	
	//싱글톤 방식으로 자바 클래스를 불러옵니다.
	ConnectDB2 connectDB = ConnectDB2.getInstance();
	if(("None").equals(kind)){
		String returns=connectDB.searchDB(phone, date, oneContent, twoContent, threeContent);
		out.print(returns);
	}
%>

