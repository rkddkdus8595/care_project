<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB4"%>
<%@ page import="org.json.simple.JSONObject" %>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String depth = request.getParameter("depth"); // 뎁스
	String getID=request.getParameter("getID"); // 매니저 아이디
	String content=request.getParameter("content"); // 뎁스에 등록될 내용
	String oldContent=request.getParameter("oldContent"); // 수정 전 내용
	String newContent=request.getParameter("newContent");
	String kind=request.getParameter("kind");
	ConnectDB4 connectDB = ConnectDB4.getInstance();
	if(("insert").equals(kind)) {
		String returns=connectDB.insertDB(depth, getID, content);
		out.print(returns);
	}else if(("update").equals(kind)) {
		String returns=connectDB.updateDB(depth, getID, oldContent, newContent);
		out.print(returns);
	}else if(("delete").equals(kind)) {
		String returns=connectDB.deleteDB(depth, getID, oldContent);
		out.print(returns);
	}
%>

