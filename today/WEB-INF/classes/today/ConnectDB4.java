
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB4 {
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private static ConnectDB4 instance = new ConnectDB4();

	public static ConnectDB4 getInstance() {
		return instance;
	}

	public ConnectDB4() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL ���� "jdbc:mysql://localhost:3306/DB�̸�"
	private String dbId = "root"; // MySQL ���� "������ ��� root"
	private String dbPw = "Lotte1234!"; // ��й�ȣ "mysql ��ġ �� ������ ��й�ȣ"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private String sql = "";
	private String sql2 = "", sql3="", sql4="", sql5="", sql6="";
	String returns = "";
	String returns2 = "";
	String result1, storeID;
	Integer result;
	
	// -------------�߰�--------------
	public String insertDB(String depth, String getID, String content) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; �̷��� ����� ���̵� ���� ã���� �Ŀ� insert ����
			// Integer.valueOf() �̰ɷ� ��Ʈ�� -> ��Ʈ
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			returns=pstmt.toString();
			//returns="������� ����?";
			if(rs.next()) {
				storeID=rs.getString("storeID"); 
				returns="������� ����? depth : "+depth;

				if(depth.equals("1")) {
					Integer depth1=Integer.valueOf(depth);
					// insert into question(name, storeID, depth) values('����','1',1);
					sql2="insert into question(name, storeID, depth) values(?,?,?)";
					//select * from question where name='siseol' and storeID='2';
					sql3="select * from question where name=? and storeID=?"; // �ߺ��� ������ Ȯ���Ϸ���
					pstmt = conn.prepareStatement(sql3);
					pstmt.setString(1, content);
					pstmt.setString(2, storeID);
					rs = pstmt.executeQuery();
					//returns=pstmt.toString();
					if(rs.next()) {
						result1="����";
					}else {
						pstmt = conn.prepareStatement(sql2);
						pstmt.setString(1, content);
						pstmt.setString(2, storeID);
						pstmt.setInt(3, depth1);
						result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
						//returns=pstmt.toString();
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
	
	// -------------����--------------
	public String updateDB(String depth, String getID, String oldContent, String newContent) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; �̷��� ����� ���̵� ���� ã���� �Ŀ� insert ����
			// Integer.valueOf() �̰ɷ� ��Ʈ�� -> ��Ʈ
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				storeID=rs.getString("storeID"); 

				if(depth.equals("1")) {
					Integer depth1=Integer.valueOf(depth);
					// update question set name='����' where name='�ü�' and storeID='1' and depth=1;
					sql2="update question set name=? where name=? and storeID=? and depth=?";
					//select * from question where name='siseol' and storeID='2';
					sql3="select * from question where name=? and storeID=?"; // �ߺ��� ������ Ȯ���Ϸ���
					pstmt = conn.prepareStatement(sql3);
					pstmt.setString(1, newContent);
					pstmt.setString(2, storeID);
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						returns="����"; // �ߺ��� ����
					}else {
						pstmt = conn.prepareStatement(sql2);
						pstmt.setString(1, newContent);
						pstmt.setString(2, oldContent);
						pstmt.setString(3, storeID);
						pstmt.setInt(4, depth1);
						result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
						returns=String.valueOf(result);
						if(result>=1) {
							returns="����";
						}
					}
				}
			}
			
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	// -------------����--------------
	public String deleteDB(String depth, String getID, String oldContent) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; �̷��� ����� ���̵� ���� ã���� �Ŀ� insert ����
			// Integer.valueOf() �̰ɷ� ��Ʈ�� -> ��Ʈ
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			result1+="1�ܰ�"+pstmt.toString();
			if(rs.next()) {
				storeID=rs.getString("storeID"); 
				if(depth.equals("1")) {
					Integer depth1=Integer.valueOf(depth);
					// select uid from question where name='�ü�' and storeID='1' and depth=1;
					sql2="select uid from question where name=? and storeID=? and depth=?"; // �ش��ϴ� 1�ܰ迡 ���������� �����ִ¾ֵ� �� ����� ���ؼ� !!!
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1, oldContent);
					pstmt.setString(2, storeID);
					pstmt.setInt(3, 1);
					rs=pstmt.executeQuery();
					result1+="2�ܰ�"+pstmt.toString();
					if(rs.next()) { // 1�ܰ迡 �ش��ϴ� uid�� ����� �ϳ��ϰ��̱� ������ �̰ɷ� ������
						String pid=rs.getString("uid");
						// delete from question where name='�ü�' and storeID='1' and depth=1;
						sql4="delete from question where name=? and storeID=? and depth=?"; // 1�ܰ� ���������
						pstmt = conn.prepareStatement(sql4);
						pstmt.setString(1, oldContent);
						pstmt.setString(2, storeID);
						pstmt.setInt(3, 1);
						result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
						result1+="3�ܰ�"+pstmt.toString();
						if(result>=1) { 
							// select uid from question where pid='107' and storeID='1' and depth=2; // 1�ܰ��� �ڽ��� 2�ܰ��� uid ã��
							sql3="select uid from question where pid=? and storeID=? and depth=?";
							pstmt=conn.prepareStatement(sql3);
							pstmt.setString(1, pid);
							pstmt.setString(2, storeID);
							pstmt.setInt(3, 2);
							rs=pstmt.executeQuery();

							result1+="4�ܰ�"+pstmt.toString();
							while(rs.next()) { // 2�ܰ���ʹ� �������ϼ��� ������ while�� ������.
								
								String pid2=rs.getString("uid");
								// delete from question where name='�ü�' and storeID='1' and depth=1;
								sql4="delete from question where pid=? and storeID=? and depth=?";
								pstmt = conn.prepareStatement(sql4);
								pstmt.setString(1, pid);
								pstmt.setString(2, storeID);
								pstmt.setInt(3, 2);
								result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�

								result1+="5�ܰ�"+pstmt.toString();
								if(result>=1) {
									// select uid from question where pid='118' and storeID='1' and depth=3;
									sql5="select uid from question where pid=? and storeID=? and depth=?"; // 2�ܰ��� �ڽĵ� uid ã��
									pstmt=conn.prepareStatement(sql5);
									pstmt.setString(1, pid2);
									pstmt.setString(2, storeID);
									pstmt.setInt(3, 3);
									rs=pstmt.executeQuery();
									result1+="6�ܰ�"+pstmt.toString();
									while(rs.next()) {
										String pid3=rs.getString("uid");
										// delete from question where name='�ü�' and storeID='1' and depth=1;
										sql6="delete from question where pid=? and storeID=? and depth=?";
										pstmt = conn.prepareStatement(sql6);
										pstmt.setString(1, pid2);
										pstmt.setString(2, storeID);
										pstmt.setInt(3, 3);
										result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�

										result1+="7�ܰ�"+pstmt.toString();
										if(result>=1) {
											returns="����";
										}else {
											returns="����";
										}
									}
								}else {
									returns="����"; //2�ܰ谡 �̹� ������������ ����
								}
								
								
							}
						}
						
					}else {
						returns="����";
					}
					
				}
			}
			
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
}



