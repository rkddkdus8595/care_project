
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB3 {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB3 instance = new ConnectDB3();

	public static ConnectDB3 getInstance() {
		return instance;
	}

	public ConnectDB3() {

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
	JSONObject jsonMain=new JSONObject();
	String sum1,sum2;
	JSONArray jArray;
	JSONObject jObject;
	
	public String searchDB1(String depth, String getID) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			//select * from question where depth='1' and storeID=(select storeID from manager where managerID='main');

			sql = "select name from question where depth=? and storeID=(select storeID from manager where managerID=?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, depth);
			pstmt.setString(2, getID);
			rs = pstmt.executeQuery();
			int i=0;
			//returns=Integer.toString(rs.getRow());
			JSONObject jsonMain = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject;
			returns="";
			while(rs.next()) { 
				jsonObject= new JSONObject(); 
				jsonObject.put("name", rs.getString("name")); 
				jsonArray.add(jsonObject);
				
			}
			jsonMain.put("receipt", jsonArray);
			returns+=jsonMain.toString(); //제대로 들어가고 있는것 확인함 
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	public String searchDB2(String depth, String getID, String depthName1) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			//returns=pstmt.toString();
			
			if(rs.next()) {
				String storeID="";
				storeID=rs.getString("storeID"); 
				//returns="여기까진 오나? depth : "+depth;
				Integer depth1=Integer.valueOf(depth); //디비 열의 depth는 정수임
				
				// select * from question where storeID='1' and pid=(select uid from question where name='시설' and  storeID='1' and  depth=1);
				sql2="select * from question where storeID=? and depth=? and pid=(select uid from question where name=? and storeID=? and depth=?)";
				
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, depth1);
				pstmt.setString(3, depthName1);
				pstmt.setString(4, storeID);
				pstmt.setInt(5, 1);
				rs = pstmt.executeQuery();
				//returns2=pstmt.toString();
				
				int i=0;
				//returns=Integer.toString(rs.getRow());
				JSONObject jsonMain = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject;
				returns="";
				while(rs.next()) { 
					jsonObject= new JSONObject(); 
					jsonObject.put("name", rs.getString("name")); 
					jsonArray.add(jsonObject);
					
				}
				jsonMain.put("receipt", jsonArray);
				returns+=jsonMain.toString(); //제대로 들어가고 있는것 확인함 
			}
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	public String searchDB3(String depth, String getID, String depthName1, String depthName2) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			//returns=pstmt.toString();
			
			if(rs.next()) {
				String storeID="";
				storeID=rs.getString("storeID"); 
				Integer depth1=Integer.valueOf(depth); //디비 열의 depth는 정수임
				
				// select * from question where storeID='1' and depth=3 and pid=(select uid from question where name='너무 추워요'
				// and storeID='1' and depth=2 and pid=(select uid from question where name='시설' and storeID='1' and depth=1));

				sql2="select * from question where storeID=? and depth=? and pid=(select uid from question where name=? "
						+ "and storeID=? and depth=? and pid=(select uid from question where name=? and storeID=? and depth=?))";
				
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, depth1);
				pstmt.setString(3, depthName2);
				pstmt.setString(4, storeID);
				pstmt.setInt(5, 2);
				pstmt.setString(6, depthName1);
				pstmt.setString(7, storeID);
				pstmt.setInt(8, 1);
				rs = pstmt.executeQuery();
				// returns2=pstmt.toString(); 출력 ㅇㅋ
				
				int i=0;
				//returns=Integer.toString(rs.getRow());
				JSONObject jsonMain = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject;
				returns="";
				while(rs.next()) { 
					jsonObject= new JSONObject(); 
					jsonObject.put("subject", rs.getString("subject"));
					jsonObject.put("name", rs.getString("name")); 
					jsonArray.add(jsonObject);
					
				}
				jsonMain.put("receipt", jsonArray);
				returns+=jsonMain.toString(); //제대로 들어가고 있는것 확인함 
			}
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
}



