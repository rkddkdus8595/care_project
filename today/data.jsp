<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB"%>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("id");
	String pwd = request.getParameter("passwd");
	String type = request.getParameter("kind"); //로그인 요청인지 회원가입 요청인지를 구분하여 메서드를 실행하도록 합니다.
	//싱글톤 방식으로 자바 클래스를 불러옵니다.
	//out.println(id);
	ConnectDB connectDB = ConnectDB.getInstance();
	if(("login").equals(type)) {
		String returns = connectDB.logindb(id, pwd);
		out.print(returns);
	} 
%>

