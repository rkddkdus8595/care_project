<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB7"%>
<%@ page import="org.json.simple.JSONObject" %>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String depth = request.getParameter("depth"); // 뎁스
	String store=request.getParameter("store"); // 점 이름 
	String depthName1=request.getParameter("depthName1"); // 1단계 이름
	String depthName2=request.getParameter("depthName2");
	ConnectDB7 connectDB = ConnectDB7.getInstance();
	if(("1").equals(depth)) {
		String returns=connectDB.searchDB1(depth, store);
		out.print(returns);
	}else if(("2").equals(depth)) {
		String returns=connectDB.searchDB2(depth, store, depthName1);
		out.print(returns);
	}else if(("3").equals(depth)) {
		String returns=connectDB.searchDB3(depth, store, depthName1, depthName2);
		out.print(returns);
	}
%>

