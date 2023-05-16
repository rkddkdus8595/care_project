<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB6"%>
<%@ page import="org.json.simple.JSONObject" %>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String depth = request.getParameter("depth"); // 뎁스
	String getID=request.getParameter("getID"); // 매니저 아이디
	String content=request.getParameter("content"); // 뎁스에 등록될 내용 (insert에서 사용할 내용)
	String subject=request.getParameter("subject"); // 층수 이런 큰 타이틀 ! (insert에서 사용할 내용)
	String depthName=request.getParameter("depthName"); // 1단계 뎁스의 이름
	String depthNameTwo=request.getParameter("depthNameTwo"); // 2단계 뎁스의 이름
	String oldTitle=request.getParameter("oldTitle"); // 수정 전 타이틀
	String newTitle=request.getParameter("newTitle"); // 수정 후 타이틀
	String oldContent=request.getParameter("oldContent"); // 수정 전 내용
	String newContent=request.getParameter("newContent"); // 수정 할 내용
	String kind=request.getParameter("kind");
	ConnectDB6 connectDB = ConnectDB6.getInstance();
	if(("insert").equals(kind)) {
		String returns=connectDB.insertDB(depth, getID, content, depthName, depthNameTwo, subject);
		out.print(returns);
	}else if(("update").equals(kind)) { // depth, id, oldTitle, newTitle, oldContent, newContent, depthNameOne, depthNameTwo, "update"
		String returns=connectDB.updateDB(depth, getID, oldTitle, newTitle, oldContent, depthName, depthNameTwo, newContent);
		out.print(returns);
	}else if(("delete").equals(kind)) {
		String returns=connectDB.deleteDB(depth, getID, subject, content, depthName, depthNameTwo);
		out.print(returns);
	}
%>

