<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB8"%>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String phoneNum = request.getParameter("phoneNum"); // 뎁스
	String depthNameOne=request.getParameter("depthNameOne"); // 매니저 아이디
	String depthNameTwo=request.getParameter("depthNameTwo");
	String subjectName=request.getParameter("subjectName");
	String depthName3=request.getParameter("depthName3"); // 얘랑
	String storeName=request.getParameter("storeName"); // 얘는 구분자를 통해 구분해야함, 즉 배열을 구분자가 들어있는 문자열로 통째로 들어옴
	String kind=request.getParameter("kind"); 
	ConnectDB8 connectDB = ConnectDB8.getInstance();
	if(("final").equals(kind)) {
		String returns=connectDB.insertDB(phoneNum, depthNameOne, depthNameTwo, subjectName, depthName3, storeName);
		out.print(returns);
	}
%>

