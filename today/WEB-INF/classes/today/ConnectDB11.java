
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB11 {
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private static ConnectDB11 instance = new ConnectDB11();

	public static ConnectDB11 getInstance() {
		return instance;
	}

	public ConnectDB11() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL ���� "jdbc:mysql://localhost:3306/DB�̸�"
	private String dbId = "root"; // MySQL ���� "������ ��� root"
	private String dbPw = "Lotte1234!"; // ��й�ȣ "mysql ��ġ �� ������ ��й�ȣ"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null, rs2=null, rs3=null, rs4=null;
	private String sql = "";
	private String sql1="", sql2 = "", sql3="", sql4="", sql5="";
	String returns = "";
	String returns2 = "", storeID="", returnTest="", depth1Name="", depth2Name="", depth3Name="";
	JSONObject jsonMain=new JSONObject();
	String sum1,sum2;
	JSONArray jArray;
	JSONObject jObject;
	
	
	// select storeID, count(*) from receipt where DATE(date) between DATE('2021-01-01') and DATE('2021-01-31') group by storeID;

	// -------------���� ������--------------  // ���� ��� �����
	public String searchDB3(String year1, String month1, String day1, String year2, String month2, String day2, String getID, 
			String storeName, String tabPosition) { // ���۰����ڴ� ��� ���� �� �� �� �ֱ� ������ !!!
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

			sql="select storeID, count(*) as max_score from receipt where DATE(date) between DATE(?) and DATE(?) group by storeID";

			pstmt = conn.prepareStatement(sql);
			sum1=year1+"-"+month1+"-"+day1;
			sum2=year2+"-"+month2+"-"+day2;
			pstmt.setString(1, sum1);
			pstmt.setString(2, sum2);
			rs = pstmt.executeQuery();
			returnTest+=pstmt.toString();
			
			int i=0;
			
			JSONObject jsonMain = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject;
			returns="";
			while(rs.next()) { 
				String max_score=Integer.toString(rs.getInt("max_score"));
				storeID=rs.getString("storeID");
				
				// select storeName from store where storeID='A1';
				sql1="select storeName from store where storeID=?";
				
				pstmt = conn.prepareStatement(sql1);
				pstmt.setString(1, storeID);
				rs2 = pstmt.executeQuery();
				
				jsonObject= new JSONObject(); // �̷��� �ϴϱ� �ߺ� �ż� �ȵ��� �ߵ� ,,,
				
				if(rs2.next()) {
					jsonObject=new JSONObject();
					jsonObject.put("storeName", rs2.getString("storeName")); 
					jsonObject.put("max_score",max_score);
					jsonArray.add(jsonObject);
				}		
				jsonMain.put("receipt", jsonArray);
			}
			returns2+=jsonMain.toString();
			returns+=jsonMain.toString();
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
				if (rs2 != null)try {rs2.close();} catch (SQLException ex) {}
				if (rs3 != null)try {rs3.close();} catch (SQLException ex) {}
				if (rs4 != null)try {rs4.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
}



