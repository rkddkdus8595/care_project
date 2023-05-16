
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB7 {
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private static ConnectDB7 instance = new ConnectDB7();

	public static ConnectDB7 getInstance() {
		return instance;
	}

	public ConnectDB7() {

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
	
	public String searchDB1(String depth, String store) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select name from question where depth=1 and storeID=(select storeID from store where storeName='����');
			
			sql = "select name from question where depth=? and storeID=(select storeID from store where storeName=?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, depth);
			pstmt.setString(2, store);
			rs = pstmt.executeQuery();
			int i=0;
			
			JSONObject jsonMain = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject;
			returns="";
			while(rs.next()) { 
				jsonObject= new JSONObject(); 
				jsonObject.put("name", rs.getString("name")); 
				jsonArray.add(jsonObject);
				
			}
			jsonMain.put("question", jsonArray);
			returns+=jsonMain.toString(); //����� ���� �ִ°� Ȯ���� 
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	public String searchDB2(String depth, String store, String depthName1) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			
			sql="select storeID from store where storeName=?"; // ���� �ش� ���̸��� ���ڵ带 ��ȸ
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, store);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				String storeID="";
				storeID=rs.getString("storeID"); // ���ڵ� ����
				Integer depth1=Integer.valueOf(depth); //��� ���� depth�� ������
				
				// select name from question where storeID='1' and depth=2 and pid=(select uid from question where name='�ü�' and storeID='1' and depth=1);

				sql2="select name from question where storeID=? and depth=? and pid=(select uid from question where name=? and storeID=? and depth=?)";
				
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
				jsonMain.put("question", jsonArray);
				returns+=jsonMain.toString(); //����� ���� �ִ°� Ȯ���� 
			}
			
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	public String searchDB3(String depth, String store, String depthName1, String depthName2) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

			sql="select storeID from store where storeName=?"; // ���� �ش� ���̸��� ���ڵ带 ��ȸ
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, store);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				String storeID="";
				storeID=rs.getString("storeID"); 
				Integer depth1=Integer.valueOf(depth); //��� ���� depth�� ������
				
				// select * from question where storeID='1' and depth=3 and pid=(select uid from question where name='�̲�������' 
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
				jsonMain.put("question", jsonArray);
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



