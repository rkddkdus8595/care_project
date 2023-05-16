
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB3 {
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private static ConnectDB3 instance = new ConnectDB3();

	public static ConnectDB3 getInstance() {
		return instance;
	}

	public ConnectDB3() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL ���� "jdbc:mysql://localhost:3306/DB�̸�"
	private String dbId = "root"; // MySQL ���� "������ ��� root"
	private String dbPw = "Lotte1234!"; // ��й�ȣ "mysql ��ġ �� ������ ��й�ȣ"
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
			returns+=jsonMain.toString(); //����� ���� �ִ°� Ȯ���� 
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
				//returns="������� ����? depth : "+depth;
				Integer depth1=Integer.valueOf(depth); //��� ���� depth�� ������
				
				// select * from question where storeID='1' and pid=(select uid from question where name='�ü�' and  storeID='1' and  depth=1);
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
				returns+=jsonMain.toString(); //����� ���� �ִ°� Ȯ���� 
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
				Integer depth1=Integer.valueOf(depth); //��� ���� depth�� ������
				
				// select * from question where storeID='1' and depth=3 and pid=(select uid from question where name='�ʹ� �߿���'
				// and storeID='1' and depth=2 and pid=(select uid from question where name='�ü�' and storeID='1' and depth=1));

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
				// returns2=pstmt.toString(); ��� ����
				
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
				returns+=jsonMain.toString(); //����� ���� �ִ°� Ȯ���� 
			}
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
}



