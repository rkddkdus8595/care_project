<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB3"%>
<%@ page import="org.json.simple.JSONObject" %>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String depth = request.getParameter("depth"); // 뎁스
	String getID=request.getParameter("getID"); // 매니저 아이디
	String depthName1=request.getParameter("depthName1");
	String depthName2=request.getParameter("depthName2");
	ConnectDB3 connectDB = ConnectDB3.getInstance();
	if(("1").equals(depth)) {
		String returns=connectDB.searchDB1(depth, getID);
		out.print(returns);
	}else if(("2").equals(depth)) {
		String returns=connectDB.searchDB2(depth, getID, depthName1);
		out.print(returns);
	}else if(("3").equals(depth)) {
		String returns=connectDB.searchDB3(depth, getID, depthName1, depthName2);
		out.print(returns);
	}
%>

