<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB10"%>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String groupName = request.getParameter("groupName"); 
	String kind=request.getParameter("kind");
	ConnectDB10 connectDB = ConnectDB10.getInstance();
	String str="yes";
	if(("manager").equals(kind)) {
		String returns=connectDB.searchDB(groupName);
		out.print(returns);
	}else if(("customer").equals(kind)) {
		String returns=connectDB.searchDB1(groupName);
		out.print(returns);
	}
%>

