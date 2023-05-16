
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB8 {
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private static ConnectDB8 instance = new ConnectDB8();

	public static ConnectDB8 getInstance() {
		return instance;
	}

	public ConnectDB8() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL ���� "jdbc:mysql://localhost:3306/DB�̸�"
	private String dbId = "root"; // MySQL ���� "������ ��� root"
	private String dbPw = "Lotte1234!"; // ��й�ȣ "mysql ��ġ �� ������ ��й�ȣ"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private String sql = "";
	private String sql1="", sql2 = "", sql3="", sql4="", sql5="";
	String returns = "";
	String returns2 = "";
	String result1, storeID, pid;
	Integer result;
	String content="", contentStr="";
	
	// -------------�߰�--------------
	public String insertDB(String phoneNum, String depthNameOne, String depthNameTwo, String subjectName, String depthName3, String storeName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select storeID from store where storeName=?"; // ���� �ش� ���̸��� ���ڵ带 ��ȸ
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, storeName);
			rs=pstmt.executeQuery();
			returns+="0�ܰ�� ������"+pstmt.toString();

			if(rs.next()) {
				String storeID="";
				storeID=rs.getString("storeID"); // ���ڵ� ����
				
				String[] subject=subjectName.split(","); // 3�ܰ��� ���� �������ϼ� �ֱ� ������ !
				String[] depth3=depthName3.split(",");
				contentStr="";
				sql1="select uid from question where name=? and storeID=? and depth=1"; // 1������ uid �������
				pstmt=conn.prepareStatement(sql1);
				pstmt.setString(1, depthNameOne);
				pstmt.setString(2,  storeID);
				rs=pstmt.executeQuery();
				returns+="2:"+pstmt.toString();
				if(rs.next()) {
					String oneUID=rs.getString("uid"); // 1�ܰ��� UID
					
					sql1="select uid from question where name=? and storeID=? and pid=? and depth=2"; // 2������ uid
					pstmt=conn.prepareStatement(sql1);
					pstmt.setString(1, depthNameTwo);
					pstmt.setString(2, storeID);
					pstmt.setString(3, oneUID);
					rs=pstmt.executeQuery();
					returns+="2:"+pstmt.toString();
					if(rs.next()) {
						String twoUID=rs.getString("uid"); // 2�ܰ��� UID
						//contentStr+=oneUID+","+twoUID+","; // �ܰ�� ','�� �����ϰ� 3�ܰ� �������� ;�� �����Ѵ�
						for(int i=0; i<subject.length;i++) {
							//returns+="for �ܰ�� ������"+depth3[i];
							sql2="select * from detail where storeID=? and name=?"; // ������ ������ ã�Ƽ� uid�� ���� ���� !!!
							pstmt=conn.prepareStatement(sql2);
							pstmt.setString(1, storeID);
							pstmt.setString(2, depth3[i]);
							rs=pstmt.executeQuery();
							if(rs.next()) {
								contentStr+=rs.getString("uid")+";"; // �ڵ�� �����ϱ� ���� uid�� ��������
							}
						}
						contentStr = contentStr.substring(0, contentStr.length()-1);
						//returns+=contentStr;
						sql3="insert into receipt(phone, depth1, depth2, depth3, process, processT, date, storeID) values(?,?,?,?,'N',null,sysdate(),?)";
						
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, phoneNum);
						pstmt.setString(2, oneUID);
						pstmt.setString(3, twoUID);
						pstmt.setString(4, contentStr);
						pstmt.setString(5, storeID);
						result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
						returns+=pstmt.toString();
						if(result>=1) {
							result1="����";
						}
					}
					
				}
				
			}
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return result1;
	}
	
	
}



