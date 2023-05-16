<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="today.ConnectDB9"%>
<%
	// 자바파일이 필요하므로 위 코드처럼 임포트합니다.
%>
<%
	request.setCharacterEncoding("UTF-8");
	String groupName = request.getParameter("groupName"); // 관리자용 그룹명
	String groupName2= request.getParameter("groupName2"); // 고객용 그룹명
	String storeID=request.getParameter("storeID"); 
	String storeName=request.getParameter("storeName");
	String id=request.getParameter("id");
	String passwd=request.getParameter("passwd"); 
	
	ConnectDB9 connectDB = ConnectDB9.getInstance();
	String flag="yes";
	
	if(("yes").equals(flag)) {
		String returns=connectDB.insertDB(groupName, groupName2, storeID, storeName, id, passwd);
		out.print(returns);
	}
%>

