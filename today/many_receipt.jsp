<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB11"%>
<%@ page import="org.json.simple.JSONObject" %>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String year1 = request.getParameter("year1");
	String month1 = request.getParameter("month1");
	String day1 = request.getParameter("day1");
	String year2 = request.getParameter("year2");
	String month2 = request.getParameter("month2");
	String day2 = request.getParameter("day2");
	String getID = request.getParameter("getID"); // 고객은 여기에 폰번호를 넣어올 것이고 관리자는 아이디를 넣어올 것
	String storeName=request.getParameter("storeName"); 
	String tabPosition = request.getParameter("tabPosition"); // 탭 위치
	String kind=request.getParameter("kind");
	
	ConnectDB11 connectDB = ConnectDB11.getInstance();
	if(("super").equals(kind) && ("전체").equals(storeName)) {
		String returns=connectDB.searchDB3(year1, month1, day1, year2, month2, day2, getID, storeName, tabPosition);
		out.print(returns);
	}
	
%>

