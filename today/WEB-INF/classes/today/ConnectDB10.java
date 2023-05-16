package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB10 {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB10 instance = new ConnectDB10();

	public static ConnectDB10 getInstance() {
		return instance;
	}

	public ConnectDB10() {

	}

	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL 계정 "jdbc:mysql://localhost:3306/DB이름"
	private String dbId = "root"; // MySQL 계정 "로컬일 경우 root"
	private String dbPw = "Lotte1234!"; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private String sql = "";
	private String sql2 = "";
	String returns = "";
	String returns2 = "";


	public String searchDB(String groupName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			
			// select storeName from store where groupName='서울';
			sql = "select storeName, groupName from store where groupName=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupName);
			rs = pstmt.executeQuery();
			JSONObject jsonMain = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject;
			returns="";
			while(rs.next()) {
				jsonObject= new JSONObject(); 
				jsonObject.put("groupName", rs.getString("groupName"));
				jsonObject.put("storeName", rs.getString("storeName")); 
				jsonArray.add(jsonObject);
			}
			jsonMain.put("store", jsonArray);
			returns+=jsonMain.toString(); //제대로 들어가고 있는것 확인함 

		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	public String searchDB1(String groupName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			
			// select storeName from store where groupName='서울';
			sql = "select storeName, groupName2 from store where groupName2=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupName);
			rs = pstmt.executeQuery();
			JSONObject jsonMain = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject;
			returns="";
			while(rs.next()) {
				jsonObject= new JSONObject(); 
				jsonObject.put("groupName", rs.getString("groupName2"));
				jsonObject.put("storeName", rs.getString("storeName")); 
				jsonArray.add(jsonObject);
			}
			jsonMain.put("store", jsonArray);
			returns+=jsonMain.toString(); //제대로 들어가고 있는것 확인함 

		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
}


